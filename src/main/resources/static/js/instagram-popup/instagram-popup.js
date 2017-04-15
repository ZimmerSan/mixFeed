jQuery(function ($) {

    /**
     * Private vars
     */
    var $body = $('body');

    var readPhotoExtended = function (photo_id, callback) {
        $.ajax({
            url: "/api/photos/" + photo_id + "/extended",
            success: function (data) {
                callback(data);
            }
        });
    };

    var showPrev = function() {
        var curIndex = $('.carouselGallery-carousel.active').data('index');
        var prevItemIndex = parseInt(curIndex - 1);
        showItem(prevItemIndex);
    };

    var showNext = function() {
        var curIndex = $('.carouselGallery-carousel.active').data('index');
        var nextItemIndex = parseInt(curIndex + 1);
        showItem(nextItemIndex);
    };

    var showItem = function(index) {
        var item = $('.carouselGallery-carousel[data-index=' + index + ']');
        if (item.length > 0) {
            $('.picture.carouselGallery-carousel').removeClass('active');
            $('body').find('.carouselGallery-wrapper').remove();

            var photo_id = $(item.get(0)).data('photo-id');
            readPhotoExtended(photo_id, showModal);
            item.first().addClass('active');
        }
        updateArrows(index);
    };

    var updateArrows = function (index) {
        var $rightBtn = $('.carouselGallery-right');
        var $leftBtn = $('.carouselGallery-left');
        $rightBtn.removeClass('disabled');
        $leftBtn.removeClass('disabled');

        var curIndex = index >= 0 ? index : $('.carouselGallery-carousel.active').data('index');
        updateArrows.nbrOfItems = updateArrows.nbrOfItems || $('.carouselGallery-carousel').length - 1;

        curIndex === updateArrows.nbrOfItems && $rightBtn.addClass('disabled');
        curIndex === 0 && $leftBtn.addClass('disabled');
    };

    $body.on('click', '.carouselGallery-carousel', function (e) {
        scrollTo = $('body').scrollTop();
        $body.addClass('noscroll');
        $body.css('position', 'fixed');
        $('.carouselGallery-col-1, .carouselGallery-col-2').removeClass('active');
        $(this).addClass('active');

        var photo_id = $(this).data('photo-id');
        readPhotoExtended(photo_id, showModal);
        updateArrows();
    });

    $body.on('click', '.carouselGallery-right', function (e) {
        showNext();
    });

    $body.on('click', '.carouselGallery-left', function (e) {
        showPrev();
    });

    var modalHtml = '';
    showModal = function (photo) {
        var username = photo.user.firstName + " " + photo.user.lastName,
            location = '',
            imagetext = 'Image Description 1',
            likes = '1234',
            imagepath = photo.photo604,
            post_url = "https://vk.com/photo-" + photo.group.id + "_" + photo.id,
            user_url = "https://vk.com/" + photo.user.domain;

        maxHeight = $(window).height() - 100;

        if ($('.carouselGallery-wrapper').length === 0) {
            if (typeof imagepath !== 'undefined') {
                modalHtml = "<div class='carouselGallery-wrapper'>";
                modalHtml += "<div class='carouselGallery-modal'><span class='carouselGallery-left'><span class='icons icon-arrow-left6'></span></span><span class='carouselGallery-right'><span class='icons icon-arrow-right6'></span></span>";
                modalHtml += "<div class='container'>";
                modalHtml += "<span class='icons iconscircle-cross close-icon'></span>";
                modalHtml += "<div class='carouselGallery-scrollbox' style='max-height:" + maxHeight + "px'><div class='carouselGallery-modal-image'>";
                modalHtml += "<img src='" + imagepath + "' alt='carouselGallery image'>";
                modalHtml += "</div>";
                modalHtml += "<div class='carouselGallery-modal-text'>";
                modalHtml += "<span class='carouselGallery-modal-group'>" +
                                "<img src='" + photo.group.photo200 + "'/>" +
                                "<a href='" + post_url + "'>" + photo.group.name + "</a>" +
                             "</span>";
                modalHtml += "<span class='carouselGallery-modal-user'>by <a href='" + user_url + "'>" + username + "</a></span>";
                // modalHtml += "<span class='carouselGallery-item-modal-likes'>";
                // modalHtml += "<span class='icons icon-heart'></span>";
                // modalHtml += "<a href='" + post_url + "'>" + likes + "</a>";
                // modalHtml += "</span>";
                // modalHtml += "<span class='carouselGallery-modal-imagetext'>";
                // modalHtml += "<p>" + imagetext + "</p></span>";
                modalHtml += "</div></div></div></div></div>";
                $('body').append(modalHtml).fadeIn(2500);
            }
        }
    };

    $body.on('click', '.carouselGallery-wrapper', function (e) {
        if ($(e.target).hasClass('carouselGallery-wrapper')) {
            removeModal();
        }
    });
    $body.on('click', '.carouselGallery-modal .iconscircle-cross', function (e) {
        removeModal();
    });

    var removeModal = function () {
        $body.find('.picture.carouselGallery-carousel').removeClass('active');
        $body.find('.carouselGallery-wrapper').remove();
        $body.removeClass('noscroll');
        $body.css('position', 'static');
        $body.animate({scrollTop: scrollTo}, 0);
    };

    // Avoid break on small devices
    var carouselGalleryScrollMaxHeight = function () {
        if ($('.carouselGallery-scrollbox').length) {
            maxHeight = $(window).height() - 100;
            $('.carouselGallery-scrollbox').css('max-height', maxHeight + 'px');
        }
    };

    $(window).resize(function () { // set event on resize
        clearTimeout(this.id);
        this.id = setTimeout(carouselGalleryScrollMaxHeight, 100);
    });

    document.onkeydown = function (evt) {
        evt = evt || window.event;
        if (evt.keyCode == 27) {
            removeModal();
        } else if (evt.keyCode === 37) {
            showPrev();
        } else if (evt.keyCode === 39) {
            showNext();
        }
    };

});
