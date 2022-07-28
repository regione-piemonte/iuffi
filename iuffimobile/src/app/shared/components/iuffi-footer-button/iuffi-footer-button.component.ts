import { Component, EventEmitter, Input, Output } from '@angular/core';

/***
 * Wrapper dello ion-button con stile custom
 */
@Component({
    selector: 'iuffi-footer-button',
    templateUrl: './iuffi-footer-button.component.html',
    styleUrls: ['./iuffi-footer-button.component.scss'],
})
export class IuffiFooterButtonComponent {
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

    @Output() public clickButton = new EventEmitter();

    public action(): void {
        this.clickButton.emit();
    }
}
