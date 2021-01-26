<#macro login path isRegisterForm>
<form action="${path}" method="post">
    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> User Name:</label>
        <div class="col-sm-5">
            <input type="text" name="username"
                   class="form-control ${(usernameError??)?string('is-invalid','')}"
                   placeholder="Username"
                   value="<#if user??>${user.username}</#if>"/>
            <#if usernameError??>
            <div class="invalid-feedback">${usernameError}</div>
        </#if>
    </div>
    </div>

    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> Password:</label>
        <div class="col-sm-5">
            <input type="password" name="password"
                   class="form-control ${(passwordError??)?string('is-invalid','')}"
                   placeholder="Password"
                   value="<#if user??>${user.password}</#if>"/>
            <#if passwordError??>
            <div class="invalid-feedback">${passwordError}</div>
        </#if>
    </div>
    </div>
    <#if isRegisterForm>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> Password2:</label>
        <div class="col-sm-5">
            <input type="password" name="password2"
                   class="form-control ${(password2Error??)?string('is-invalid','')}"
                   placeholder="Password2"
                   value="<#if password2??>${password2}</#if>"/>
            <#if password2Error??>
            <div class="invalid-feedback">${password2Error}</div>
        </#if>
    </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> Email:</label>
        <div class="col-sm-5">
            <input type="email" name="email"
                   class="form-control ${(emailError??)?string('is-invalid','')}"
                   placeholder="Email"
                   value="<#if user??>${user.email}</#if>"/>
            <#if emailError??>
            <div class="invalid-feedback">${emailError}</div>
        </#if>
    </div>
    </div>
    <div class="form-group row">
        <div class="col-sm-5">
            <div class="g-recaptcha" data-sitekey="6LdLEuQZAAAAAAFXXvQxb8btWP6452v6E7bjMqHb"></div>
            <#if captchaError??>
            <div class="alert alert-danger" role="alert">
                ${captchaError}
            </div>
        </#if>
        </div>
    </div>
</#if>

<input type="hidden" name="_csrf" value="${_csrf.token}"/>
<button type="submit" class="btn btn-primary"><#if isRegisterForm>Create<#else>Sign in</#if></button>
<div class="mt-4">
    <#if !isRegisterForm>
    <a href="/registration"> Add new User</a>
</#if>
</div>
</form>
</#macro>

<#macro logout isRegisterUser>
<!--Spring внутреннимим процессами обработает logout (должен быть post)-->
<form action="/logout" method="post" style="margin: 0">
    <button type="submit" class="btn btn-primary"><#if isRegisterUser>Logout<#else>Log in</#if></button>
    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
</form>
</#macro>