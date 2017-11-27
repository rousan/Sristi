var winH,
    winW;

winH = parseInt(window.innerHeight, 10);
winW = parseInt(window.innerWidth, 10);


document.documentElement.style.height = winH + "px";
document.documentElement.style.width = winW + "px";

document.body.style.height = winH + "px";
document.body.style.width = winW + "px";

document.getElementById("mask").style.height = winH + "px";
document.getElementById("mask").style.width = winW + "px";