
// 导航栏
function loadNavation(domName, isOpen, jsonData) {
    let flag = "";
    if (!isOpen) {
        flag = "mdui-drawer-close";
        document.body.classList.remove("mdui-drawer-body-left")
    } else {
        document.body.classList.add("mdui-drawer-body-left")
    }
    let navTemp = "";

    for (let key in jsonData) {
        navTemp += `
        <li class="mdui-list-item mdui-ripple" onclick="loadPage('${key}', apiData)">
            <i class="mdui-list-item-icon mdui-icon material-icons mdui-text-color-theme">&#xe0e2;</i>
            <div class="mdui-list-item-content">${key}</div>
        </li>`
    }

    document.getElementsByTagName("body")[0].innerHTML +=
        `<div class="mdui-container ">
        <div class="mdui-drawer ${flag}" id="drawer" >
            <ul class="mdui-list mdui-collapse" mdui-collapse>
                ${navTemp}
            </ul>
        </div>
    </div>`;
}

// 载入一个页面
function loadPage(key, apiData) {
    let apiList = apiData[key];
    domId("apiContainer").innerHTML = "";
    apiList.forEach(function (apiItem) {
        domId("apiContainer").innerHTML += loadApi(apiItem);
    })
}

// 载入 API
function loadApi(apiItem) {
    let apiName = apiItem.name;
    if (apiName === "") {
        apiName = "接口名称"
    }
    let apiUrl = "";
    for (let i = 0; i < apiItem.url.length; i++){
        if(apiItem.url == null){
            continue;
        }
        apiUrl += `<li><code>${apiItem.url[i]}</code></li>`;
    }
    let apiMethod = "";
    for (let i = 0; i < apiItem.method.length; i++){
        if(apiItem.method == null){
            continue;
        }
        apiMethod += `<li><code>${apiItem.method[i]}</code></li>`
    }
    let apiParams = "";
    for (let i = 0; i < apiItem.params.length; i++){
        if(apiItem.params[i] == null){
            continue;
        }
        apiParams +=
            `<tr>
                <td>${apiItem.params[i].value}</td>
                <td>${apiItem.params[i].returnType}</td>
                <td>${apiItem.params[i].required}</td>
                <td>${apiItem.params[i].defaultValue}</td>
                <!--<td>${apiItem.params[i].note}</td>-->
                <td>接口备注</td>
            </tr>`
    }

    let HTML =
        `<div class="juzcar-api-container">
            <div class="juzcar-api-title">
                <div>
                    <h2>${apiName}</h2>
                    <ul>
                        <li>
                            <p>请求地址</p>
                            <ul>
                                ${apiUrl}
                            </ul>
                        </li>
                        <li>
                            <p>请求方法</p>
                            <ul>
                                ${apiMethod}
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
            <div>
                <div class="mdui-table-fluid">
                    <table class="mdui-table">
                        <thead>
                        <tr>
                            <th>参数</th>
                            <th>类型</th>
                            <th>是否必须</th>
                            <th>默认值</th>
                            <th>备注</th>
                        </tr>
                        </thead>
                        <tbody>
                            ${apiParams}
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="mdui-divider"></div>
        </div>`;

    return HTML;
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