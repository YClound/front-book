var netChart = function(){
    //设置node样式
    function nodeStyle(node){
        if(node.data.isInput){
            node.labelStyle.textStyle.fillColor = "#fff";
        }
    }

    //弹出框显示
    var modalModule = function(modaldata,degreeLists,index){
        var html = '<td></td>';
        var length = modaldata.length;
        var modal = '<div class="modal fade" id="myModal'+index+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
                        '<div class="modal-dialog">'+
                            '<div class="modal-content">'+
                                '<div class="modal-header">'+
                                    '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'+
                                    '<h4 class="modal-title" id="myModalLabel">查看关联系数</h4>'+
                                '</div>'+
                                '<div class="modal-body">'+
                                    '<table></table>'+
                                '</div>'+
                            '</div>'+
                        '</div>'+
                    '</div>';
        $("body").append(modal);
        $.each(modaldata,function(index, element) {
            html += '<td>'+element.name+'</td>'
        });
        $("#myModal"+index).find("table").html('<tr>'+html+'</tr>');
        for(var i=0; i<length; i++){
            var tdHtml = '<td>'+modaldata[i].name+'</td>';
            for(var n=0; n<length; n++){
                if(i==n){
                    tdHtml += '<td>-</td>';
                }else{
                    var relateTd = '<td>0</td>';
                    if(degreeLists.length){
                        $.each(degreeLists,function(index,degreeList){
                            if((modaldata[i].id == degreeList.startId && modaldata[n].id == degreeList.endId) || (modaldata[i].id == degreeList.endId && modaldata[n].id == degreeList.startId)){
                                if(degreeList.degree){
                                    relateTd = '<td>' + degreeList.degree+'</td>';
                                } 
                            }
                        });
                    }
                    tdHtml += relateTd;
                }
            }
            $("#myModal"+index).find("table").append('<tr>'+tdHtml+'</td>'); 
        }

    } 
    return {
        init : function(data,index){
            var nodes=[], links=[];
            var message = data.message,
                index = index + 1;
            var chartId = ("chart" + index),
                degreeLists = data.degreeList;
            // 关联群组
            var chnNumChar = {
                          "1" : "一",
                          "2" : "二",
                          "3" : "三",
                          "4" : "四",
                          "5" : "五",
                          "6" : "六",
                          "7" : "七",
                          "8" : "八",
                          "9" : "九"
                        };
            var nodeList = data.nodeList;
            var linkList = data.linkList;
            var inputNumber = 0;
            // 弹出框数据
            var modaldata = [];
            // 获取nodes
            $.each(nodeList, function(index,node){
                var nodeStyle = {};
                //判断节点样式
                if(node.isInput){
                    nodeStyle = {
                        "fillColor" : "#002060",
                        "label" : node.name
                    };
                    inputNumber ++;
                    modaldata.push(node);
                }else{
                    nodeStyle = {
                        "lineColor" : "#5f9ed7",
                        "fillColor" : "#ffffff",
                        "label" : node.name
                    };
                }
                nodes.push({
                            "id" : node.id,
                            "isInput" : node.isInput,
                            "style" : nodeStyle
                        });
            });

            //获取links
            $.each(linkList,function(index,link){
                var linkStyle = {};
                var linkPosition = link.position || "--";
                var linkShareholdingRatio;
                //判断投资是否为空
                if(link.shareholdingRatio){
                    linkShareholdingRatio = link.shareholdingRatio.toFixed(2) + "%";
                }else{
                    linkShareholdingRatio = "--";
                };

                //判断linkLabel的显示
                if(link.relateName == "任职"){
                    linkStyle = {
                        "label" : linkPosition,
                        "fillColor" : "#002060",
                        "toDecoration": "arrow",
                        "lineDash": [10, 10]
                    }
                }else if(link.relateName == "投资"){
                    linkStyle = {
                        "label" : linkShareholdingRatio,
                        "fillColor" : "#5f9ed7",
                        "toDecoration": "arrow"
                    }
                };
                links.push(
                    {
                        "from" : link.startId,
                        "to" : link.endId,
                        "style" : linkStyle
                    }
                );      
            });
            // 实例化netChart对象
            var chart = new NetChart({
                container : document.getElementById(chartId),
                area : {height : 550},
                data : {preloaded : {
                    nodes : nodes,
                    links : links
                }},
                style : {
                    node : {"display" : "text"},
                    linkLabel:{
                        textStyle:{font:"20px Arial",fillColor : "#000"},
                        backgroundStyle:{lineColor:"#fff"}
                    },
                    nodeLabel : {
                        padding : 8,
                        textStyle:{font:"20px Arial"}
                    },
                    nodeStyleFunction : nodeStyle
                },
                layout : {
                    "mode" : "hierarchy",
                    "nodeSpacing" : 45,
                    "rowSpacing" : 250
                }
            });

            //网状图标题和提示
            var chartTileDiv = '<div class="chartTitle">'+
                '<h4>关联族群'+chnNumChar[index]+'：'+message+'</h4>'+
                '</div>';
            var chartImage = '<div class="chartImage"><img src= "../assets/image/img.png"/></div>'
            $("#"+chartId).before(chartTileDiv);
            $("#"+chartId).after(chartImage);

            // 输入节点大于2个时显示弹出框
            if(inputNumber > 2){
                modalModule(modaldata,degreeLists,index);
                $("#"+chartId).siblings(".chartTitle").append('<a class="btn btn-warning" id="showRelation" href="javascript:;" data-toggle="modal" data-target="#myModal'+index+'">查看关联系数</a>');
            }else{//两个输入节点时
                var relateNum = 0;
                $.each(degreeLists,function(index,degreeList){
                    if((modaldata[0].id == degreeList.startId && modaldata[1].id == degreeList.endId) || (modaldata[1].id == degreeList.startId && modaldata[0].id == degreeList.endId)){
                        if(degreeList.degree){
                            relateNum = degreeList.degree;
                        }  
                    }
                })
                $("#"+chartId).siblings(".chartTitle").append('<a class="btn btn-warning" id="showRelation" href="javascript:;">'+relateNum+'度关联</a>')
            }
        }
    }
}();

$(function(){
    //显示搜索结果
    var result = function(data){
        var totalCount = data.totalCount,
            foundCount = data.foundCount,
            notFoundCount = data.notFoundCount,
            notFoundList = data.notFoundList.join(","),
            groupCount = data.groupCount,
            coverEntCount = data.coverEntCount,
            notRelatedCount = data.notRelatedCount,
            notRelatedList =data. notRelatedList.join(",");
        var html;
        if(totalCount == foundCount){
            if(notRelatedCount == 0){
                html = '<h3>本次共输入'+totalCount+'个节点，有'+groupCount+'个关联群组，覆盖'+coverEntCount+'家企业</h3>';
            }else{
               html = '<h3>本次共输入'+totalCount+'个节点，有'+groupCount+'个关联族群，覆盖'+coverEntCount+'家企业，剩余'+notRelatedCount+'个节点与其他节点无关联。</h3>'+
                       ' <p style="line-height: 30px; padding-bottom: 20px;">'+notRelatedCount+'个节点分别是：'+notRelatedList+'</p>';
            }
        }else{
            if(notRelatedCount == 0){
                html = ' <h3>本次共输入'+totalCount+'个节点，成功找到'+foundCount+'个，其中'+notFoundCount+'个节点未查询到。</h3>'+
                        '<p style="line-height: 30px; padding-bottom: 20px;">其中，未查询的节点是：'+notFoundList+'。在'+foundCount+'个节点中，有'+groupCount+'个关联族群，覆盖'+coverEntCount+'家企业。</p>';
            }else{
                html = '<h3>本次共输入'+totalCount+'个节点，成功找到'+foundCount+'个，其中'+notFoundCount+'个节点未查询到。</h3>'+
                        ' <p style="line-height: 30px; padding-bottom: 20px;">其中，未查询的节点是：'+notFoundList+'。其中有'+groupCount+'个关联族群，覆盖'+coverEntCount+'家企业，剩余'+notRelatedCount+'个节点与其他节点无关联。<br>'+notRelatedCount+'个节点分别是：'+notRelatedList+'</p>';
            }
        }
        $(".contentTitle").html(html);
    }
    // 请求数据
    $.get("../assets/data/data.json", function(relativeGroup){
        if(relativeGroup.code == "0000"){
            var data = relativeGroup.data;
            if(data.success && data.groupList.length){
                var groupList = data.groupList;
                result(data);
                //加载网状图
                $.each(groupList, function(index,dataList){
                    $(".chartList").append('<div class="chartItem"><div id="chart'+(index + 1)+'" class="chart"></div></div>');
                    netChart.init(dataList,index);
                })
            }else{
                $(".contentTitle").html('<div class="chartContent">没有搜索到相关信息！</div>'); 
            }
        }else{
            $(".contentTitle").html('<div class="chartContent">暂无相关信息！</div>');
        }
    })
})