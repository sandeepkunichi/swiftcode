/// <reference path="jquery-3.1.1.min.js" />
/// <reference path="p5.js" />
/// <reference path="three.js" />
/// <reference path="perlin.js" />
/// <reference path="stats.min.js" />
/// <reference path="controls/OrbitControls.js" />


$(document).ready(function (e) {

    $("#form-signup").wrap();

    $('#file-input').change(function () {
        var filepath = $('#file-input').val();
        var filename = filepath.replace(/\\$/, '').split('\\').pop();
        console.log(filename);
        if (filename.length == 0) {
            filename = "Upload Resume";
        }
        if (filename.length >= 20) {
            filename = filename.substr(0, 17) + "...";

        }
        $('#file-input-label').html(filename);
    });



});





// THREE.JS

var SCREEN_WIDTH = window.innerWidth;
var SCREEN_HEIGHT = window.innerHeight * 3 / 5;
var SCREEN_ASPECT_RATIO = SCREEN_WIDTH / SCREEN_HEIGHT;

var container, loader, stats;
var renderer, camera, scene;
var controls;
var ambientLight, pointLight;

var lineGeometry;
var lineMaterial;
var lineMesh;
var pointLightHelper;

var fov = 30;
var cameraHeight = 1000;
var mouseX = 0, mouseY = 0;
var planeHeight = 50, planeWidth = 80;
var lightY = 20;

var frame = 0;
var squareSize = 1;

var pointArray = [];



function init() {

    // Global Variables
    noise.seed(Math.random());
    container = document.getElementById("canvas-container");
    canvas = document.getElementById("main-canvas");
    loader = new THREE.JSONLoader();
    stats = new Stats();
    stats.domElement.id = "canvas-stats";

    container.appendChild(stats.domElement);

    // Renderer
    renderer = new THREE.WebGLRenderer({ canvas: canvas, antialias: true });
    renderer.setClearColor(0x000000);
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.shadowMap.enabled = true;
    renderer.shadowMap.type = THREE.PCFSoftShadowMap;
    renderer.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

    // Camera and Controls
    camera = new THREE.PerspectiveCamera(fov, SCREEN_ASPECT_RATIO, 0.1, 1000);
    //camera = new THREE.OrthographicCamera(-40, 40, 10, -10, 0, 100);
    camera.position.set(0, 40, 70);
    camera.lookAt(new THREE.Vector3(0, 0, 15));


    //Scene
    scene = new THREE.Scene();

    //Lights
    ambientLight = new THREE.AmbientLight(0xffffff, 0.5);
    pointLight = new THREE.PointLight(0xffffff, 0.5);
    pointLight.castShadow = true;
    pointLight.shadow.bias = 0.001;
    pointLight.shadow.mapSize.width = 1024;
    pointLight.shadow.mapSize.height = 1024;
    pointLight.position.set(0, lightY, 0);


    // Geometry
    lineGeometry = new THREE.Geometry();
    var ifac, jfac;
    for (var i = -planeHeight; i < planeHeight; i += squareSize) {
        ifac = (i + planeHeight) / (2 * planeHeight);
        for (var j = -planeWidth; j < planeWidth; j += squareSize) {
            jfac = sigmoid(4 * j / planeWidth);

            if (i < planeHeight - squareSize) {
                lineGeometry.vertices.push(new THREE.Vector3(j, 0, i));
                lineGeometry.vertices.push(new THREE.Vector3(j, 0, i + squareSize));
                lineGeometry.colors.push(new THREE.Color((1 - jfac), (1 - jfac) * 0.35 + jfac * 0.3, jfac * 1));
                lineGeometry.colors.push(new THREE.Color((1 - jfac), (1 - jfac) * 0.35 + jfac * 0.3, jfac * 1));
            }

            if (j < planeWidth - squareSize) {
                lineGeometry.vertices.push(new THREE.Vector3(j, 0, i));
                lineGeometry.vertices.push(new THREE.Vector3(j + squareSize, 0, i));
                lineGeometry.colors.push(new THREE.Color((1 - jfac), (1 - jfac) * 0.35 + jfac * 0.3, jfac * 1));
                lineGeometry.colors.push(new THREE.Color((1 - jfac), (1 - jfac) * 0.35 + jfac * 0.3, jfac * 1));
            }
        }
    }

    lineGeometry.verticesNeedUpdate = true;

    //Textures
    blueOrangeTexture = new THREE.TextureLoader().load("/images/blue_orange_gradient.png");

    // Materials
    lineMaterial = new THREE.MeshBasicMaterial({ vertexColors: THREE.VertexColors });

    // Mesh
    lineMesh = new THREE.LineSegments(lineGeometry, lineMaterial);

    // Helpers
    pointLightHelper = new THREE.PointLightHelper(pointLight, 0.1);

    //Add Stuff to Scene
    scene.add(ambientLight);
    scene.add(pointLight);
    scene.add(pointLightHelper);

    scene.add(lineMesh)


}

function animate() {
    requestAnimationFrame(animate);
    render();
    stats.update();
    frame++;
}

function render() {

    if (frame % 4 == 0) {
        var perlinFactor = 0.05;
        var verticesArray = lineMesh.geometry.vertices;

        for (var i = 0; i < verticesArray.length; i++) {
            var vertex = verticesArray[i];
            vertex.y = noise.simplex3(vertex.x * perlinFactor, vertex.z * perlinFactor, Date.now() / 20000) * 5;
        }
        lineMesh.geometry.verticesNeedUpdate = true;
        renderer.render(scene, camera);
    }
}

function onWindowResize() {
    SCREEN_WIDTH = window.innerWidth;
    SCREEN_HEIGHT = window.innerHeight * 2 / 3 - 50;
    SCREEN_ASPECT_RATIO = SCREEN_WIDTH / SCREEN_HEIGHT;
    camera.aspect = SCREEN_ASPECT_RATIO;
    camera.updateProjectionMatrix();
    renderer.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
}

function onDocumentMouseMove(event) {
    mouseX = event.clientX;
    mouseY = event.clientY - 50;
}

function sigmoid(t) {
    return 1 / (1 + Math.pow(Math.E, -t));
}

//Event Handlers
window.addEventListener("resize", onWindowResize);
document.addEventListener('mousemove', onDocumentMouseMove, false);

init();
animate();


$("#file-input").on('change', function () {
    var size = this.files[0].size / 1024;
    if (size > 512) {
        $("#signup-message").html("File size too big. Maximum size is 512KB");
        $("#file-input").val("");
        $("#file-input-label").val("Upload Resume");
    }
});

$("#signup-button").on("click", function () {

    var ext = $('#file-input').val().split('.').pop().toLowerCase();

    if ($('#file-input').val() && $.inArray(ext, ['pdf']) == -1) {
        $('#signup-message').html("Please select a PDF file");
        return;
    }

    var formData = new FormData($('form')[0]);

    $('#signup-message').html('Registering...');

    $.ajax({
        url: '/register',
        type: 'POST',
        success: function (data) {
            $('#signup-message').html(data);
            $('#form-signup')[0].reset();
            $('#file-input-label').html("Upload Resume");
        },
        error: function (data) { },
        data: formData,
        cache: false,
        contentType: false,
        processData: false
    });
});