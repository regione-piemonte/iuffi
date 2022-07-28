import { Directive, HostListener } from '@angular/core';

@Directive({
    selector: '[appIntegerInput]'
})
export class IntegerInputDirective {

    constructor() { }

    @HostListener('input', ['$event'])
    public onInput(event: InputEvent): boolean {
        const pattern = /[0-9]+/g;
        const inputChar = event.data;

        if (inputChar && !pattern.test(inputChar)) {
            event.preventDefault();
            return false;
        }
        return true;
    }

}
