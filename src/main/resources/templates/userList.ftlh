<#import "parts/common.ftlh" as c>
<@c.page>
<h1>List of Users</h1>
<table class="table table-striped">
    <thead class="thead-dark">
    <tr>
        <th>Name</th>
        <th>Role</th>
        <th></th>
        <th></th>

    </tr>
    </thead>
    <tbody>
    <#list users as user>
    <tr>
        <td>${user.username}</td>
        <td><#list user.roles as role>${role}<#sep>, </#list></td>
        <td><a href="/user/${user.id}">Edit</a></td>
        <td>
<!--            <a href="/user/delete/${user.id}">Delete</a>-->
            <a href="" data-toggle="modal" data-target="#exampleModal_${user.id}">Delete</a>
            <div class="modal fade" id="exampleModal_${user.id}" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Notification</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            Are you sure you want to delete this user?
                            <br/>
                            If yes, click DELETE button
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <a href="/user/delete/${user.id}" type="button" class="btn btn-primary">Delete</a>
                        </div>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    </#list>
    </tbody>
</table>
</@c.page>