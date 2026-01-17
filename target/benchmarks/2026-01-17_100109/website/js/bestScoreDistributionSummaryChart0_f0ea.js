
var chart_bestScoreDistributionSummaryChart0_f0ea = new Chart(document.getElementById('chart_bestScoreDistributionSummaryChart0_f0ea'), {
    type: 'boxplot',
    data: {
        labels: [
            'Problem_0', 'Problem_1', 'Problem_2'
        ],
        datasets: [
                {
                    label: 'Late Acceptance',
                        borderWidth: 1
                    ,
                    data: [
                                {
                                    min: -1,
                                    max: -1,
                                    q1: -1,
                                    q3: -1,
                                    median: -1,
                                    mean: -1,
                                    items: [],
                                    outliers: [],
                                }
                            , 
                                {
                                    min: -1,
                                    max: -1,
                                    q1: -1,
                                    q3: -1,
                                    median: -1,
                                    mean: -1,
                                    items: [],
                                    outliers: [],
                                }
                            , 
                                {
                                    min: 0,
                                    max: 0,
                                    q1: 0,
                                    q3: 0,
                                    median: 0,
                                    mean: 0,
                                    items: [],
                                    outliers: [],
                                }
                            
                    ]
                }, 
                {
                    label: 'Tabu Search (favorite)',
                        borderWidth: 4
,
                    data: [
                                {
                                    min: -1,
                                    max: -1,
                                    q1: -1,
                                    q3: -1,
                                    median: -1,
                                    mean: -1,
                                    items: [],
                                    outliers: [],
                                }
                            , 
                                {
                                    min: -1,
                                    max: -1,
                                    q1: -1,
                                    q3: -1,
                                    median: -1,
                                    mean: -1,
                                    items: [],
                                    outliers: [],
                                }
                            , 
                                {
                                    min: 0,
                                    max: 0,
                                    q1: 0,
                                    q3: 0,
                                    median: 0,
                                    mean: 0,
                                    items: [],
                                    outliers: [],
                                }
                            
                    ]
                }
        ]
    },
    options: {
        animation: false,
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            title: {
                display: true,
                text: 'Best hard score distribution summary (higher is better)'
            }
        },
        scales: {
            x: {
                display: true
            },
            y: {
                title: {
                    display: true,
                    text: 'Best hard score'
                },
                display: true
            }
        },
watermark: {
    image: "website/img/timefold-logo-stacked-positive.svg",
    x: 15,
    y: 15,
    width: 48,
    height: 50,
    opacity: 0.1,
    alignX: "right",
    alignY: "bottom",
    alignToChartArea: true,
    position: "front",
}    },
plugins: [{ 
    id: 'customPlugin',
    beforeDraw: (chart, args, options) => {
          const ctx = chart.canvas.getContext('2d');
          ctx.save();
          ctx.globalCompositeOperation = 'destination-over';
          ctx.fillStyle = 'white';
          ctx.fillRect(0, 0, chart.canvas.width, chart.canvas.height);
          ctx.restore();
    }
}]
});

window.addEventListener('beforeprint', () => {
  chart_bestScoreDistributionSummaryChart0_f0ea.resize(1280, 720);
});
window.addEventListener('afterprint', () => {
  chart_bestScoreDistributionSummaryChart0_f0ea.resize();
});