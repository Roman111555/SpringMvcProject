<a class="btn btn-primary mb-3" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
    Message edit
</a>
<div class="collapse <#if message??>show</#if>" id="collapseExample">
    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data" class="form-group">
            <div class="form-group">
                <input class="form-control col-sm-5" type="hidden" name="_csrf" value="${_csrf.token}"/>
            </div>
            <div class="form-group">
                <input class="form-control col-sm-5 ${(textError??)?string('is-invalid','')}"
                       type="text" name="text" placeholder="Enter name"
                       value="<#if message??>${message.text}</#if>"/>
                <#if textError??>
                <div class="invalid-feedback">${textError}</div>
                 </#if>
             </div>
    <div class="form-group">
        <input class="form-control col-sm-5 ${(tagError??)?string('is-invalid','')}"
               type="text" name="tag" placeholder="Enter tag" value="<#if message??>${message.tag}</#if>"/>
        <#if tagError??>
        <div class="invalid-feedback">${tagError}</div>
    </#if>
</div>
<div class="form-group">
    <div class="custom-file col-sm-5">
        <input type="file" name="file" id="customFile"/>
        <label class="custom-file-label" for="customFile">Choose files</label>
    </div>
</div>
<div class="form-group">
    <button class="btn btn-primary" type="submit">Save message</button>
</div>
</form>
</div>
</div>
