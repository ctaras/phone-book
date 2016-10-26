$(function () {
    var profileLink = $('#link-profile');
    if(profileLink.text() == "") {
        profileLink.html(profileLink.data("text"))
    }
});

$(function () {
    $(document).on('click', '#link-logout', function (event) {
        event.preventDefault();
        $('#form-logout').submit();
    });
});