<#include "security.ftlh">
<div class="card-columns" id="message-list">
    <#list messages as message>
    <div class="card my-3" data-id="${message.id}">
            <#if message.filename??>
            <img src="/img/${message.filename}" class="card-img-top"/>
        </#if>
    <div class="m-2">
        <span>${message.text}</span><br/>
        <i>#${message.tag}</i>
    </div>
    <div class="card-footer text-muted">
        <a href="/user-messages/${message.author.id}">${message.authorName}</a>
        <#if message.author.id == currentUserId>
        <a class="btn btn-primary" href="/user-messages/${message.author.id}?messageId=${message.id}">Edit</a>
        <a class="btn btn-danger" data-toggle="modal" data-target="#exampleModal_${message.id}">Delete</a>
        <div class="modal fade" id="exampleModal_${message.id}" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Notification</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete this message?
                        <br/>
                        If yes, click DELETE button
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <a class="btn btn-danger" href="/delete/user-messages/${message.author.id}?messageId=${message.id}">Delete</a>
                    </div>
                </div>
            </div>
        </div>
        </#if>
    </div>
</div>
<#else>
No message
</#list>
</div>