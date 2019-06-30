import 'bootstrap/dist/css/bootstrap.min.css'
import 'chart.js/dist/Chart.css'
import '../styles/gapi-fonts/icon.css'
import '../styles/shards-dashboards.1.1.0.css'
import 'bootstrap-table/dist/bootstrap-table.min.css'
import 'bootstrap'
import 'bootstrap-table/dist/bootstrap-table.min'
import 'bootstrap-table/dist/bootstrap-table-locale-all.min'
import 'chart.js/dist/Chart.bundle.min'
import 'sockjs-client/dist/sockjs.min'

const SockJS = require("sockjs-client/dist/sockjs.min");
const Stomp = require("stompjs");
const Turbolinks = require("turbolinks");
window.SockJS = SockJS;
window.Stomp = Stomp;
Turbolinks.start();