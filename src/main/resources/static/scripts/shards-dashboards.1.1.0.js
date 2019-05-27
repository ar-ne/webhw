!function (t, o) {
    "object" == typeof exports && "undefined" != typeof module ? o() : "function" == typeof define && define.amd ? define(o) : o()
}(0, function () {
    "use strict";
    if ("undefined" == typeof Chart) throw new Error("Shards Dashboard requires the Chart.js library in order to function properly.");
    window.ShardsDashboards = window.ShardsDashboards ? window.ShardsDashboards : {}, $.extend($.easing, {
        easeOutSine: function (t, o, e, i, n) {
            return i * Math.sin(o / n * (Math.PI / 2)) + e
        }
    }), Chart.defaults.LineWithLine = Chart.defaults.line, Chart.controllers.LineWithLine = Chart.controllers.line.extend({
        draw: function (t) {
            if (Chart.controllers.line.prototype.draw.call(this, t), this.chart.tooltip._active && this.chart.tooltip._active.length) {
                var o = this.chart.tooltip._active[0], e = this.chart.ctx, i = o.tooltipPosition().x,
                    n = this.chart.scales["y-axis-0"].top, r = this.chart.scales["y-axis-0"].bottom;
                e.save(), e.beginPath(), e.moveTo(i, n), e.lineTo(i, r), e.lineWidth = .5, e.strokeStyle = "#ddd", e.stroke(), e.restore()
            }
        }
    })
});