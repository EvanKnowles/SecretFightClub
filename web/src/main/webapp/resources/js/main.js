$(function () {

    $(document).on('click', '.movie', function () {
        var $this = $(this);
        var id = $this.find('.movie-id').val();
        $('.submitted-id').val(id);
        $('.shown-movie-name').text($this.find('.movie-name').text());
        $('.shown-movie-description').text($this.find('.movie-description').val());
        $('.lightbox.show-movie').addClass('shown');
        if ($this.find('.booking-now').length == 0) {
            $('.subscribe').show();
        } else {
            $('.subscribe').hide();
        }
    });
    $(document).on('click', '.show-subscribes', function () {
        $('.lightbox.subscribes').addClass('shown');
    });

    hide = function () {
        $('.lightbox').removeClass('shown')
    };
    $(document).on('click', '.lightbox', hide);
    $(document).on('touchstart', '.lightbox', hide);

    $(document).on('touchstart', '.popup', function (e) {
        e.stopPropagation();
    });
    $(document).on('click', '.popup', function (e) {
        e.stopPropagation();
    });
});