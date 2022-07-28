import { Component, Input } from '@angular/core';

/***
 * Wrapper dello ion-button con stile custom
 */
@Component({
    selector: 'iuffi-button',
    templateUrl: './iuffi-button.component.html',
    styleUrls: ['./iuffi-button.component.scss'],
})
export class IuffiButtonComponent {
    /**
     * Testo del button tradotto tramite ngx-translate
     */
    @Input() public label = '';
    /**
     * Colore di background definiti da Ionic.
     *
     * Quando è 'primary' imposta il testo del button 'light'
     * e viceversa
     */
    @Input() public color = '';
    /**
     * Attributo size, default normal
     */
    @Input() public size = '';
    /**
     * Attributo expand, default 'block'
     */
    @Input() public expand = 'block';
    /**
     * Attributo disabled, default false
     */
    @Input() public disabled = false;
    /**
     * Attributo icon, default ''
     */
    @Input() public icon = '';
}
