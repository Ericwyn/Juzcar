
function gotoApiPage(path) {
    window.location.href = path;
}

// 导出按钮的 onclick
function outPutDocument() {
    outPutHTMLDocument();
    // TODO 其他导出方式选择
}

// 导出 HTML 格式的文档
function outPutHTMLDocument() {
    window.open("/api/outPutHTMLDocument");
}

function testThisApi(uri) {
    let paramListDom = domId("paramList_"+uri);
    let paramListStr = "";
    for (let i = 0; i < paramListDom.children.length; i++){
        let paramItemDom = paramListDom.children[i];
        console.log(paramItemDom);
        let paramName = paramItemDom.children[0].innerText;
        let paramValue = paramItemDom.children[4].children[0].children[0].value;
        paramListStr += paramName+":"+paramValue+";";
    }
    ajax_post(
        "/api/testJuzcarApi",
        [
            ["uri",uri],
            ["paramList", paramListStr]
        ],
        function (respText) {
            clog(respText)
        },
        function (status) {
            clog(status)
        }
    );

    console.log(paramListStr)
}

//-------------工具方法分割---------------


function domId(domName) {
    return document.getElementById(domName);
}

function domTag(tagName) {
    return document.getElementsByTagName(tagName);
}

function domClass(className) {
    return document.getElementsByClassName(className);
}

function domName(name) {
    return document.getElementsByName(name);
}

function clog(msg) {
    console.log(msg)
}

function ajax_get(url, success_callback, fail_callback) {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.send();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                success_callback(xhr.responseText)
            } else {
                fail_callback(xhr.status)
            }
        }
    }
}

function ajax_post(url, params, success_callback, fail_callback) {
    let xhr = new XMLHttpRequest();
    xhr.open('POST', url, true);
    let formData = new FormData();
    if (params !== null) {
        for (let i = 0; i < params.length; i++) {
            formData.append(params[i][0], params[i][1])
        }
        xhr.send(formData);
    } else {
        xhr.send();
    }
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                success_callback(xhr.responseText)
            } else {
                fail_callback(xhr.status)
            }
        }
    }
}