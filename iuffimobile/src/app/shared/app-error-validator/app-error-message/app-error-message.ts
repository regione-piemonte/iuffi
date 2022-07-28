import { Component, Input } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
    selector: 'app-error-message',
    templateUrl: './app-error-message.html',
    styleUrls: ['./app-error-message.scss'],
})
export class AppErrorMessage {
    @Input() public control!: FormControl;
    public params: any;

    public get errorMessage(): string | null {
        if (this.control && this.control.errors) {
            for (const propertyName in this.control.errors) {
                if (this.control.errors.hasOwnProperty(propertyName) && this.control.touched) {
                    // Passo i parametri alla stringa di traduzione
                    this.params = this.control.errors[propertyName];
                    return `ERR_${propertyName.toUpperCase()}`;
                }
            }
        }

        return null;
    }
}
