jQuery(function ($) {
    $(document).ready(function () {

        if ($('#profile-panel').length) {
            var user_id = $('#profile-panel').data('user-id');
            getUserPhotosCount(user_id, showPhotosCountStat);
            getUserGroupsCount(user_id, showGroupsCountStat);
            getUserFriends(user_id, showFriendsCountStat);
        }

        if ($('#gallery').length) {
            var user_id = $('#profile-panel').data('user-id');
            getUserPhotos(user_id, showUserPhotos);
        }

    });

    var showUserPhotos = function (photos) {

        var num = photos.length;
        var container = $('<div />');
        for (var i = 0; i < num; i++) {
            container.append(
                '<div class="picture carouselGallery-carousel" data-index="' + i + '" data-photo-id="' + photos[i].id + '">' +
                '<figure class="effect-lexi">' +
                '<img src="' + photos[i].photo604 + '" class="all studio"/>' +
                '<figcaption>' +
                '<p>Apollos last game of pool was so strange.</p>' +
                '</figcaption>' +
                '</figure>' +
                '</div>');
        }

        $('#gallery')
            .html('<div id="gallery-content"><div id="gallery-content-center">' + container.html() + '</div></div>')
            .delay(num * 100 < 1500 ? num * 100 : 1500)
            .queue(function() {
                galleryMasonryInit();
                $('#gallery').dequeue();
            });

        $('#gallery').delay(1000).fadeTo(500, 1);

        // $('#gallery').delay(10000).fadeTo('slow', 1);
    };

    var galleryMasonryInit = function () {
        var button = 1;
        var button_class = "gallery-header-center-right-links-current";
        var $container = $('#gallery-content-center');

        $container.isotope({
            itemSelector: '.picture'
        });


        function check_button() {
            $('.gallery-header-center-right-links').removeClass(button_class);
            if (button == 1) {
                $("#filter-all").addClass(button_class);
                $("#gallery-header-center-left-title").html('All Galleries');
            }
            if (button == 2) {
                $("#filter-studio").addClass(button_class);
                $("#gallery-header-center-left-title").html('Studio Gallery');
            }
            if (button == 3) {
                $("#filter-landscape").addClass(button_class);
                $("#gallery-header-center-left-title").html('Landscape Gallery');
            }
        }

        $("#filter-all").click(function () {
            $container.isotope({filter: '.all'});
            button = 1;
            check_button();
        });
        $("#filter-studio").click(function () {
            $container.isotope({filter: '.studio'});
            button = 2;
            check_button();
        });
        $("#filter-landscape").click(function () {
            $container.isotope({filter: '.landscape'});
            button = 3;
            check_button();
        });

        check_button();
    };

    var showPhotosCountStat = function (count) {
        var stat = $('body').find('#photos-count-stat');
        stat.find('.stat-data').text(count);
        stat.fadeTo(500, 1);
    };

    var showGroupsCountStat = function (count) {
        var stat = $('body').find('#groups-count-stat');
        stat.find('.stat-data').text(count);
        stat.fadeTo(500, 1);
    };

    var showFriendsCountStat = function (friends) {
        var stat = $('body').find('#friends-count-stat');
        stat.find('.stat-data').text(friends.length);
        stat.fadeTo(500, 1);
    };

    /*
     **  AJAX functions
     */

    var getUserFriends = function (user_id, callback) {
        $.ajax({
            url: "api/users/" + user_id + "/friends/active",
            success: function (data) {
                callback(data);
            }
        });
    };

    var getUserPhotos = function (user_id, callback) {
        $.ajax({
            url: "api/users/" + user_id + "/photos",
            success: function (data) {
                callback(data);
            }
        });
    };

    var getUserPhotosCount = function (user_id, callback) {
        $.ajax({
            url: "api/users/" + user_id + "/photos/count",
            success: function (data) {
                callback(data);
            }
        });
    };

    var getUserGroupsCount = function (user_id, callback) {
        $.ajax({
            url: "api/users/" + user_id + "/groups/count",
            success: function (data) {
                callback(data);
            }
        });
    };
});