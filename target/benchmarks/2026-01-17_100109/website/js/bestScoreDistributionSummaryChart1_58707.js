
var chart_bestScoreDistributionSummaryChart1_58707 = new Chart(document.getElementById('chart_bestScoreDistributionSummaryChart1_58707'), {
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
                                    min: 2.133975,
                                    max: 2.133975,
                                    q1: 2.133975,
                                    q3: 2.133975,
                                    median: 2.133975,
                                    mean: 2.133975,
                                    items: [],
                                    outliers: [],
                                }
                            , 
                                {
                                    min: 42.38562,
                                    max: 47.73217,
                                    q1: 42.38562,
                                    q3: 47.73217,
                                    median: 44.1824,
                                    mean: 44.76673,
                                    items: [],
                                    outliers: [],
                                }
                            , 
                                {
                                    min: 185.73068,
                                    max: 186.22901,
                                    q1: 185.73068,
                                    q3: 186.22901,
                                    median: 186.02487,
                                    mean: 185.99485333333334,
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
                                    min: 2.133975,
                                    max: 2.133975,
                                    q1: 2.133975,
                                    q3: 2.133975,
                                    median: 2.133975,
                                    mean: 2.133975,
                                    items: [],
                                    outliers: [],
                                }
                            , 
                                {
                                    min: 50.8559,
                                    max: 50.8559,
                                    q1: 50.8559,
                                    q3: 50.8559,
                                    median: 50.8559,
                                    mean: 50.8559,
                                    items: [],
                                    outliers: [],
                                }
                            , 
                                {
                                    min: 184.6459,
                                    max: 186.6459,
                                    q1: 184.6459,
                                    q3: 186.6459,
                                    median: 184.6459,
                                    mean: 185.31256666666667,
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
                text: 'Best soft score distribution summary (higher is better)'
            }
        },
        scales: {
            x: {
                display: true
            },
            y: {
                title: {
                    display: true,
                    text: 'Best soft score'
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
  chart_bestScoreDistributionSummaryChart1_58707.resize(1280, 720);
});
window.addEventListener('afterprint', () => {
  chart_bestScoreDistributionSummaryChart1_58707.resize();
});