/// <reference path="jquery-3.1.1.min.js" />
/// <reference path="p5.js" />
/// <reference path="three.js" />
/// <reference path="stats.min.js" />


// Animate Logo Sizes
$(document).ready(function (e) {

    $("#training-icon").animate({
        "width": "150px",
        "height": "150px",
        "top": "0px",
        "left": "0px",
    }, 'normal');

    $("#automation-icon").delay(300).animate({
        "width": "150px",
        "height": "150px",
        "top": "0px",
        "left": "150px",
    }, 'normal');

    $("#iot-icon").delay(600).animate({
        "width": "150px",
        "height": "150px",
        "top": "150px",
        "left": "0px",
    }, 'normal');

    $("#ml-icon").delay(900).animate({
        "width": "150px",
        "height": "150px",
        "top": "150px",
        "left": "150px",
    }, 'normal');





});

$(window).on('resize', function () {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
});




// THREE.JS

var SCREEN_WIDTH = window.innerWidth;
var SCREEN_HEIGHT = window.innerHeight - 50;
var SCREEN_ASPECT_RATIO = SCREEN_WIDTH / SCREEN_HEIGHT;

var container, loader, stats;
var renderer, camera, scene;
var ambientLight, pointLight;

var creatureGeometry, planeGeometry;
var creatureMaterial, planeMaterial, emissionMaterial;
var creatureMesh, planeMesh;
var pointLightHelper;

var fov = 15;
var cameraHeight = 100;
var mouseX = 0, mouseY = 0;
var planeHeight = 2 * cameraHeight * Math.tan(THREE.Math.degToRad(fov / 2)), planeWidth = planeHeight * SCREEN_ASPECT_RATIO;
var lightY = 20;

var leftX = -planeWidth * 155 / SCREEN_WIDTH;
var rightX = planeWidth * 155 / SCREEN_WIDTH;

var topZ = -planeHeight * 155 / (SCREEN_HEIGHT - 100);
var botZ = planeHeight * 155 / (SCREEN_HEIGHT + 100);

var creatureLoaded = false;
var maxCreatures = Math.floor(SCREEN_WIDTH * SCREEN_HEIGHT / 100000);
var creatures = [];


function init() {

    console.log("Plane : " + planeWidth + "x" + planeHeight);

    console.log("Left : " + leftX);
    console.log("Right : " + rightX);
    console.log("Top : " + topZ);
    console.log("Bot : " + botZ);


    // Global Variables
    container = document.getElementById("canvas-container");
    loader = new THREE.JSONLoader();
    stats = new Stats();
    stats.domElement.id = "canvas-stats";

    container.appendChild(stats.domElement);


    // Renderer
    renderer = new THREE.WebGLRenderer({ antialias: true });
    renderer.setClearColor(0xffffff);
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.shadowMap.enabled = true;
    renderer.shadowMap.type = THREE.PCFSoftShadowMap;
    renderer.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    renderer.domElement.id = "main-canvas";
    container.appendChild(renderer.domElement);

    // Camera and Controls
    camera = new THREE.PerspectiveCamera(fov, SCREEN_ASPECT_RATIO, 0.1, 100);
    camera.position.set(0, cameraHeight, 0);
    camera.lookAt(new THREE.Vector3(0, 0, 0));

    //Scene
    scene = new THREE.Scene();

    //Lights
    ambientLight = new THREE.AmbientLight(0xffffff, 0.50);
    pointLight = new THREE.PointLight(0xffffff, 0.4);
    pointLight.castShadow = true;
    pointLight.shadow.bias = 0.001;
    pointLight.shadow.mapSize.width = 1024;
    pointLight.shadow.mapSize.height = 1024;
    pointLight.position.set(0, lightY, 0);


    // Geometry
    planeGeometry = new THREE.PlaneGeometry(planeWidth, planeHeight);


    // Materials
    creatureMaterial = new THREE.MeshLambertMaterial({ color: 0xcccccc });
    planeMaterial = new THREE.MeshPhongMaterial({ color: 0x44ccff });
    emissionMaterial = new THREE.MeshLambertMaterial();

    // Mesh
    creatureMesh;
    loader.load("/json/creature_nb.json", function (geometry) {
        creatureGeometry = geometry;
        creatureMesh = new THREE.Mesh(creatureGeometry, creatureMaterial);

        creatureMesh.scale.set(0.4, 0.4, 0.4);
        creatureMesh.castShadow = true;
        creatureLoaded = true;
        console.log("Creature Loaded..");
        console.log("Adding " + maxCreatures + " creatues");
        for (var i = 0; i < maxCreatures; i++) {
            var dir = THREE.Math.randFloat(0, 2 * Math.PI)
            var posx, posz;
            if (THREE.Math.randInt(0, 1) == 1) {
                posx = THREE.Math.randFloat(0.8 * planeWidth / 2, planeWidth / 2) * (THREE.Math.randInt(0, 1) * 2 - 1);
                posz = THREE.Math.randFloat(-planeHeight / 2, planeHeight / 2);
            }
            else {
                posx = THREE.Math.randFloat(-planeWidth / 2, planeWidth / 2);
                posz = THREE.Math.randFloat(0.8 * planeHeight / 2, planeHeight / 2) * (THREE.Math.randInt(0, 1) * 2 - 1);
            }

            creatureMesh.position.set(posx, 0.1, posz);
            creatureMesh.rotateY(dir);
            creatures.push([creatureMesh.clone(), dir]);
            scene.add(creatures[i][0]);
            creatureMesh.rotateY(-dir);
            console.log("Added");
        }
    });

    planeMesh = new THREE.Mesh(planeGeometry, planeMaterial);
    planeMesh.position.set(0, 0, 0);
    planeMesh.rotateX(THREE.Math.degToRad(-90));
    planeMesh.receiveShadow = true;

    // Helpers 
    pointLightHelper = new THREE.PointLightHelper(pointLight, 0.1);

    //Add Stuff to Scene
    //scene.add(pointLightHelper);
    scene.add(ambientLight);
    scene.add(pointLight);

    scene.add(planeMesh);



}

function animate() {
    requestAnimationFrame(animate);
    render();
    stats.update();
}

function render() {

    for (var i = 0; i < creatures.length; i++) {
        var creature = creatures[i][0];
        var dir = creatures[i][1];
        var speed = new THREE.Vector3(0, 0, 0.1);
        speed.applyAxisAngle(new THREE.Vector3(0, 1, 0), dir);
        creature.position.add(speed);

        if (creature.position.x < -planeWidth / 2) {
            var newdir = 2 * Math.PI - dir;
            creature.rotateY(newdir - dir);
            creatures[i][1] = newdir;

        }
        else if (creature.position.x > planeWidth / 2) {
            var newdir = 2 * Math.PI - dir;
            creature.rotateY(newdir - dir);
            creatures[i][1] = newdir;
        }

        if (creature.position.z < -planeHeight / 2) {
            var newdir = Math.PI - dir;
            creature.rotateY(newdir - dir);
            creatures[i][1] = newdir;
        }
        else if (creature.position.z > planeHeight / 2) {
            var newdir = Math.PI - dir;
            creature.rotateY(newdir - dir);
            creatures[i][1] = newdir;
        }

        var quad = getQuad(creature.position);
        if (quad != 0) {
            creature.position.sub(speed);
            if (quad == "NW") {
                creature.material = new THREE.MeshLambertMaterial({ color: 0xff0000 });
                if (Math.abs(creature.position.z - topZ) < Math.abs(creature.position.x - leftX)) {
                    var newdir = Math.PI - dir;
                    creature.rotateY(newdir - dir);
                    creatures[i][1] = newdir;
                }
                else {
                    var newdir = 2 * Math.PI - dir;
                    creature.rotateY(newdir - dir);
                    creatures[i][1] = newdir;
                }
            }
            else if (quad == "NE") {
                creature.material = new THREE.MeshLambertMaterial({ color: 0xffff00 });
                if (Math.abs(creature.position.z - topZ) < Math.abs(creature.position.x - rightX)) {
                    var newdir = Math.PI - dir;
                    creature.rotateY(newdir - dir);
                    creatures[i][1] = newdir;
                }
                else {
                    var newdir = 2 * Math.PI - dir;
                    creature.rotateY(newdir - dir);
                    creatures[i][1] = newdir;
                }
            }
            else if (quad == "SW") {
                creature.material = new THREE.MeshLambertMaterial({ color: 0x00ff00 });
                if (Math.abs(creature.position.z - botZ) < Math.abs(creature.position.x - leftX)) {
                    var newdir = Math.PI - dir;
                    creature.rotateY(newdir - dir);
                    creatures[i][1] = newdir;
                }
                else {
                    var newdir = 2 * Math.PI - dir;
                    creature.rotateY(newdir - dir);
                    creatures[i][1] = newdir;

                }
            }
            else {
                creature.material = new THREE.MeshLambertMaterial({ color: 0x0099ff });
                if (Math.abs(creature.position.z - botZ) < Math.abs(creature.position.x - rightX)) {
                    var newdir = Math.PI - dir;
                    creature.rotateY(newdir - dir);
                    creatures[i][1] = newdir;
                }
                else {
                    var newdir = 2 * Math.PI - dir;
                    creature.rotateY(newdir - dir);
                    creatures[i][1] = newdir;
                }
            }

        }
    }

    pointLight.position.set((planeWidth) * (mouseX - SCREEN_WIDTH / 2) / (SCREEN_WIDTH), lightY, (planeHeight) * (mouseY - SCREEN_HEIGHT / 2) / SCREEN_HEIGHT);
    renderer.render(scene, camera);
}

function onWindowResize() {
    SCREEN_WIDTH = window.innerWidth;
    SCREEN_HEIGHT = window.innerHeight - 50;
    SCREEN_ASPECT_RATIO = SCREEN_WIDTH / SCREEN_HEIGHT;
    camera.aspect = SCREEN_ASPECT_RATIO;
    camera.updateProjectionMatrix();
    renderer.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
}

function onDocumentMouseMove(event) {
    mouseX = event.clientX;
    mouseY = event.clientY - 50;
}

function getQuad(position) {


    if (position.x > leftX && position.x < rightX && position.z > topZ && position.z < botZ) {

        res = "";
        if (position.z > 0) {
            res += "S";
        }
        else {
            res += "N";
        }
        if (position.x < 0) {
            res += "W";
        }
        else {
            res += "E";
        }

        return res;

    }
    else {
        return 0;
    }

}

//Event Handlers
window.addEventListener("resize", onWindowResize);
document.addEventListener('mousemove', onDocumentMouseMove, false);

init();
animate();