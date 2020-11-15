import { Injectable } from "@angular/core";

@Injectable({
              providedIn: "root",
            })
export class DownloadService {
  downloadBinary(data: Blob, name: string): void {
    const url = window.URL.createObjectURL(data);

    const link: HTMLAnchorElement = document.createElement("a");
    link.href = url;
    link.download = name;
    link.click();
    window.URL.revokeObjectURL(url);
  }
}
