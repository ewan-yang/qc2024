import { getServiceEnvConfig } from "~/.env-config";
import { getToken } from "~/src/store/modules/auth/helpers";
const { url } = getServiceEnvConfig(import.meta.env)
export function exportExcal(
  postUrl: string,
  formParams: any,
  exportName: string,
  onSuccess: () => void
) {
  console.log(formParams, "formParams");
  var xhr = new XMLHttpRequest();
  xhr.open("POST", url + postUrl, true);
  xhr.setRequestHeader("Authorization", `Bearer ${getToken()}`);
  xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  xhr.responseType = "blob";
  xhr.onload = function (e) {
    //如果请求执行成功
    if (this.status == 200) {
      var blob = this.response;
      var a = document.createElement("a"); // blob.type = "application/octet-stream"; //创键临时url对象
      var href = URL.createObjectURL(blob);

      a.href = href;

      a.download = exportName;

      a.click(); //释放之前创建的URL对象

      window.URL.revokeObjectURL(href);
      // 添加成功回调调用
      onSuccess();
    }
  }; //发送请求
  var params = JSON.stringify(formParams);
  xhr.send(params);
}