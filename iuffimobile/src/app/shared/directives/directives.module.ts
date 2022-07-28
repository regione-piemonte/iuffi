import { NgModule } from '@angular/core';
import { IntegerInputDirective } from './integer-input.directive';

@NgModule({
    declarations: [IntegerInputDirective],
    exports: [IntegerInputDirective]
})

export class DirectivesModule { }
