$(window).load(function () {

var button = 1;
var button_class = "gallery-header-center-right-links-current";
var $container = $('#gallery-content-center');
    
$container.isotope({
	itemSelector : '.picture'
});


function check_button(){
	$('.gallery-header-center-right-links').removeClass(button_class);
	if(button==1){
		$("#filter-all").addClass(button_class);
		$("#gallery-header-center-left-title").html('All Galleries');
		}
	if(button==2){
		$("#filter-studio").addClass(button_class);
		$("#gallery-header-center-left-title").html('Studio Gallery');
		}
	if(button==3){
		$("#filter-landscape").addClass(button_class);
		$("#gallery-header-center-left-title").html('Landscape Gallery');
		}	
}
	
$("#filter-all").click(function() { $container.isotope({ filter: '.all' }); button = 1; check_button(); });
$("#filter-studio").click(function() {  $container.isotope({ filter: '.studio' }); button = 2; check_button();  });
$("#filter-landscape").click(function() {  $container.isotope({ filter: '.landscape' }); button = 3; check_button();  });

check_button();
});