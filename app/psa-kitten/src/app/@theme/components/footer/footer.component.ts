import { Component } from '@angular/core';

@Component({
  selector: 'ngx-footer',
  styleUrls: ['./footer.component.scss'],
  template: `
    <span class="created-by">
        Designed and built in cooperation with
        <a href="https://www.schule-altendorf.ch/" target="_blank">Primarschule Altendorf</a>.
    </span>
    <div class="socials">
      <a href="https://github.com/BilledTrain380/sporttag-psa" target="_blank" class="ion ion-social-github"></a>
    </div>
  `,
})
export class FooterComponent {
}
