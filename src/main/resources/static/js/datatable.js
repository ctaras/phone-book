$(document).ready(function() {
    var table = $('#contacts').DataTable( {
        "ajax": "/contacts",
        "language": {
            "emptyTable": "No contacts"
        },
        "columns": [
            { "data": "firstName"           , "title": "First name" },
            { "data": "lastName"            , "title": "Last name" },
            { "data": "mobilePhoneNumber"   , "title": "Mobile" },
            { "data": "email"               , "title": "Email" },
            { "data": "address"             , "title": "Address" },
            { "data": "middleName"          , "title": "Middle name" },
            { "data": "homePhoneNumber"     , "title": "Home phone number" },
            { "data": "id"                  , "title": "Contact id" },
            { "data": "userId"              , "title": "User id" },
            { "data": null                  , "title": "Actions", "width": "80px" }
        ],
        "columnDefs": [
            {
                "targets": [ 2 ]
                // "render": function ( data, type, row ) {
                //     return data.replace(/(\d{3})(\d{2})(\d{3})(\d{4})/, '+$1($2)$3-$4');
                // }
            },
            {
                "targets": [ 3 ],
                "visible": false,
                "searchable": false
            },
            {
                "targets": [ 4 ],
                "visible": false,
                "searchable": false
            },
            {
                "targets": [ 5 ],
                "visible": false,
                "searchable": false
            },
            {
                "targets": [ 6 ],
                "visible": false,
                "searchable": false
            },
            {
                "targets": [ 7 ],
                "visible": false,
                "searchable": false
            },                {
                "targets": [ 8 ],
                "visible": false,
                "searchable": false
            },
            {
                "targets": -1,
                "sortable": false,
                "searchable": false,
                "data": null,
                "defaultContent": "",
                "render": function ( data, type, row, meta ) {
                    return "<div class='btn-group'> " +
                        "<button type='button' class='btn btn-default btn-action-edit' aria-label='Edit' " +
                        "data-toggle='modal' data-target='#edit-dialog' data-id='" + data.id + "' " +
                        "data-rowindex='" + meta.row + "' data-title='Edit contact'> " +
                        "<span class='glyphicon glyphicon-edit' aria-hidden='true'></span> </button> " +
                        "<button type='button' class='btn btn-default btn-action-remove' aria-label='Remove' " +
                        "data-toggle='modal' data-target='#remove-dialog' data-id='" + data.id + "' " +
                        "data-contact='" + data.firstName + " " + data.lastName + "' data-rowindex='" + meta.row + "'>" +
                        " <span class='glyphicon glyphicon-remove-circle' aria-hidden='true'></span> </button> </div>";
                }
            }
        ]
    } );

    function removeRow(index) {
        table.row(index).remove().draw();
    }

    function updateRow(index, data) {
        table.row(index).data(data).draw();
    }

    function addRow(data) {
        table.row.add(data).draw();
    }

    var removeDialog = $('#remove-dialog');

    removeDialog.on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var contact = button.data('contact');
        var contactId = button.data('id');
        var rowIndex = button.data('rowindex');

        var modal = $(this);
        modal.find('#btn-remove').data("id", contactId);
        modal.find('#btn-remove').data("rowindex", rowIndex);
        modal.find('.modal-body').text('Would you like to remove "' + contact +  '"?')
    });

    removeDialog.on('click', '#btn-remove', function(event) {
        var modal = $(event.delegateTarget);
        var contactId = $(this).data('id');
        var rowIndex = $(this).data('rowindex');

        $.ajax({
            url: '/contacts/' + contactId,
            type: 'DELETE',
            success: function(result) {
                removeRow(rowIndex);
            } || $.noop,
            error: $.noop
        });

        modal.modal('hide');
    });

    var editDialog = $('#edit-dialog');

    editDialog.on('show.bs.modal', function (event) {
        $('#form-edit').validator('destroy');
        $('#form-edit').validator();
        var button = $(event.relatedTarget);
        var contactId = button.data('id');
        var rowIndex = button.data('rowindex');
        var title = button.data('title');

        $('#edit-dialog-error ul').html("");

        var modal = $(this);
        modal.find('#btn-edit').data("rowindex", rowIndex);
        modal.find('#editModalLabel').text(title);

        if(contactId != -1) {
            $.get("/contacts/" + contactId, function(data, status){
                $('#edit-dialog input').each(function( index ) {
                    var input = $( this );
                    var key = input.attr("name");

                    var value = "";
                    if(key == "contactId") {
                        value = data["id"];
                    } else if(data[key]) {
                        value = data[key];
                    }

                    input.val(value);
                });
            });
        } else {
            $('#edit-dialog input').each(function( index ) {
                $( this ).val("");
            });
        }
    });

    $('#form-edit').validator().on('submit', function (e) {
        if (!e.isDefaultPrevented()) {
            e.preventDefault();

            var modal = $('#edit-dialog');
            var button = $('#edit-dialog #btn-edit');

            var rowIndex = button.data('rowindex');

            var json = {
                "id" : "",
                "userId" : "",
                "lastName" : "",
                "firstName" : "",
                "middleName" : "",
                "mobilePhoneNumber" : "",
                "homePhoneNumber" : "",
                "address" : "",
                "email" : ""
            };

            $('#edit-dialog input').each(function() {
                var input = $( this );
                var key = input.attr("name");
                var value = input.val();

                if(key == "contactId") {
                    json["id"] = value;
                } else {
                    json[key] = value;
                }
            });

            var url = "/contacts";
            if(rowIndex != -1) {
                url = url + "/" + json["id"];
            }

            $.post( url, json)
                .done(function( data ) {
                    if(rowIndex == -1) {
                        addRow(data);
                    } else {
                        updateRow(rowIndex, data);
                    }
                    modal.modal('hide');

                }).fail(function(xhr, status, error) {
                var list = $('#edit-dialog-error ul');
                list.html("");
                $.each(xhr.responseJSON.errors, function(index, value) {
                    var field = value.field
                    field = field.toLowerCase().replace(/\b[a-z]/g, function(letter) {
                        return letter.toUpperCase();
                    });
                    list.append('<li class="list-group-item"><span class="text-danger">' + field + ': ' + value.message.toLowerCase() + '</span></li>');
                });
            });
        }
    });

    $(function($){
        $("#inputModilePhoneNumber").mask("+999(99) 999-9999");
        $("#inputHomePhoneNumber").mask("+999(99) 999-9999");
        //$('#form-edit').validator()
    });

    editDialog.on('click', '#btn-edit', function(event) {
        event.preventDefault();
        $('#form-edit').submit();
    });
} );

$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});
