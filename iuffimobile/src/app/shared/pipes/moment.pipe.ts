import { Pipe } from '@angular/core';
import * as moment from 'moment';

@Pipe({
    name: 'moment'
})
export class MomentPipe {
    public transform(value: string | number, args: string): string {
        args = args || '';
        return moment(new Date(value)).format(args);
    }
}
