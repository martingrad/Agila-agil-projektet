arel.sceneReady(function()
{
	console.log("sceneReady");

	//set a listener to tracking to get information about when the image is tracked
	arel.Events.setListener(arel.Scene, function(type, param){trackingHandler(type, param);});

	//get the car model reference
	var model = arel.Scene.getObject("augmentation");
	var aidModel = arel.Scene.getObject("visualization");
	
	arel.Events.setListener(model, function(id, type, args) {
		console.log("type = "+type+"; args="+JSON.stringify(args));
	});
	arel.Events.setListener(aidModel, function(id, type, args) {
		console.log("type = "+type+"; args="+JSON.stringify(args));
	});
	
	arel.Scene.loadEnvironmentMap("Assets/Custom/env_map.png");
});

function clickHandler()
{
    if (document.getElementById('radio1').checked)
    {
		arel.Scene.sensorCommand("reset", "", function(tmp){});
    }
};

function trackingHandler(type, param)
{
	//check if there is tracking information available
	if(param[0] != undefined)
	{
		//if the pattern is found, hide the information to hold your phone over the pattern
		if(type && type == arel.Events.Scene.ONTRACKING && param[0].getState() == arel.Tracking.STATE_TRACKING)
		{
			console.log("received STATE_TRACKING from tracking");
		}
	}
};

