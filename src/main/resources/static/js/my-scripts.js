jQuery(function ($) {
    $(document).ready(function () {
        var profile_panel = $('#profile-panel');

        if (profile_panel.length) {
            var user_id = profile_panel.data('user-id');
            getUserPhotosCount(user_id, showPhotosCountStat);
            getUserGroupsCount(user_id, showGroupsCountStat);
            getUserFriends(user_id, showFriendsCountStat);
         }

    });

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