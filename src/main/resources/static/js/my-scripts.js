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

        if ($('#groups-table').length) {
            showGroups();
        }
    });

    var $body = $('body');

    $body.delegate('form.refresh-group', 'submit', function (event) {
        event.preventDefault();
        let $form = $(event.currentTarget);
        let $button = $form.find('button[name=group_id]');
        let $count = $form.closest('.group-row').find('.group-photos');
        let group_id = $button.val();

        var csrf_token = $("meta[name='_csrf']").attr("content");
        var csrf_header = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            type: "PUT",
            beforeSend: function (request) {
                request.setRequestHeader(csrf_header, csrf_token);
                $button.fadeOut();
                $count.text("loading...");
            },
            complete: function () {
                $button.fadeIn();
            },
            url: 'api/groups/' + group_id,
            data: $form.serialize(), // serializes the form's elements. Needed for CSRF token
            success: function (data) {
                $count.text(data.photosCount);
            }
        });
    });

    var showGroups = function () {
        $.get('api/groups', function (groups) {
            $.get('mustache/templates.htm', function (templates) {
                var template = $(templates).filter('#tpl-group-row').html();

                var tbody = $('<tbody />');
                for (let group of groups) {
                    tbody.append(Mustache.render(template, group));
                }
                $body.find('#groups-table').append(tbody);

                //used to enable checkboxes
                callWhenReady('#groups-table tbody', function () {
                    $('[data-toggle="checkbox"]').each(function () {
                        var $checkbox = $(this);
                        $checkbox.checkbox();
                    });
                });
            });
        });
    };

    var showUserPhotos = function (photos) {
        $.get('mustache/templates.htm', function (templates) {
            var template = $(templates).filter('#tpl-photo-small').html();
            var $gallery_content_center = $('#gallery-content-center');

            var $photos = $();
            var num = photos.length;
            for (var i = 0; i < num; i++) {
                var photo = photos[i];
                photo.i = i;
                $photos = $photos.add(Mustache.render(template, photo));
            }

            $gallery_content_center.append($photos);
            $gallery_content_center.waitForImages(function () {
                $gallery_content_center.isotope({
                    itemSelector: '.picture'
                });
                $('#gallery').delay(500).fadeTo('slow', 1);
            });
        });
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

    var getAllGroups = function (user_id, callback) {
        $.ajax({
            url: "api/groups",
            success: function (data) {
                callback(data);
            }
        });
    };

    var callWhenReady = function (selector, callback, scope) {
        if (!$(selector).length) return;
        var self = this;
        if ($(selector).closest('body').length) {
            callback.call(scope);
        } else {
            setTimeout(function () {
                self.callWhenReady(selector, callback, scope);
            }, 1);
        }
    }

});