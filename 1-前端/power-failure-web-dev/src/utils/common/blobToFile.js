import { getToken } from '@/store/modules/auth/helpers';

export function blobToFile(file) {
  return new Promise((resolve, reject) => {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', file.fileUrl, true);
    xhr.setRequestHeader('Authorization', `Bearer ${getToken()}`);
    xhr.responseType = 'blob';
    xhr.onload = function (e) {
      if (this.status == 200) {
        const blob = this.response;
        let name = file.fileName;
        const fileType = new File([blob], name, { lastModified: Date.now() });
        resolve(fileType);
      } else {
        reject(new Error('Failed to fetch the file.'));
      }
    };
    xhr.onerror = function () {
      reject(new Error('Failed to fetch the file.'));
    };
    xhr.send();
  });
}