/**window.onload = function() {
	handleStateChange();
};

window.onhashchange = function() {
	handleStateChange();
};

function handleStateChange() {
	var hash = window.location.hash;
	var sectionToDisplay = null;

	if(hash.toLowerCase().indexOf('enterurl') != -1) {
		sectionToDisplay = document.querySelector('.enter-url');
	} else if(hash.toLowerCase().indexOf('about') != -1) {
		sectionToDisplay = document.querySelector('.about-info');
	} else if(hash.toLowerCase().indexOf('loading') != -1) {
		sectionToDisplay = document.querySelector('.loading');
	}

	var currentDisplayed = document.querySelector('.displayed');
	if(currentDisplayed) {
		currentDisplayed.classList.remove('displayed');
	}

	if(sectionToDisplay != null) {
		sectionToDisplay.classList.add('displayed');
	}
}**/

function showEnterUrl() {
	changeDisplayed('.enter-url');
}

function showLoadingSpinner() {
	changeDisplayed('.loading');
}

function showAbout() {
	changeDisplayed('.about-info');
}

function showNoPane() {
	removeCurrentPane();
}

function changeDisplayed(className) {
	sectionToDisplay = document.querySelector(className);

	removeCurrentPane();

	if(sectionToDisplay != null) {
		sectionToDisplay.classList.add('displayed');
	}
}

function removeCurrentPane() {
	var currentDisplayed = document.querySelector('.displayed');
	if(currentDisplayed) {
		currentDisplayed.classList.remove('displayed');
	}
}