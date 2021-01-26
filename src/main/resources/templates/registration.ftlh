<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>
<div class="mb-1">Add new User</div>
<span>${message?ifExists}</span>
<@l.login "/registration" true/>
</@c.page>