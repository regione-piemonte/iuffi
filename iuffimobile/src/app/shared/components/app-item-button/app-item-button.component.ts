import { Component, Input, ViewEncapsulation } from '@angular/core';

/***
 * Wrapper dello ion-item di tipo button con stile custom
 */
@Component({
    selector: 'app-item-button',
    templateUrl: './app-item-button.component.html',
    styleUrls: ['./app-item-button.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class AppItemButtonComponent {
    /**
     * Testo del button tradotto tramite ngx-translate
     */
    @Input() public label = '';
    /**
     * Colore di background definiti da Ionic.
     *
     * Quando è 'primary' imposta il testo del button 'light' e viceversa
     * Quando è 'success' o 'danger' imposta il testo del button a default
     * Quando è 'inactive'  imposta il testo 'light'
     */
    @Input() public color = 'primary';
    /**
     * Altezza dell'item in pixel
     */
    @Input() public height = '48';
    /**
     * Classi che vengono applicate direttamente alla <ion-label>
     */
    @Input() public labelClass = '';
    /**
     * Attributo disabled, default false
     */
    @Input() public disabled = false;
    /**
     * Allineamento testo
     */
    @Input() public alignment = 'center';
}
