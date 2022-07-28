import { Injectable, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable()
export class AutoUnsubscribe implements OnDestroy {
    public destroy$: Subject<boolean> = new Subject<boolean>();

    public ngOnDestroy(): void {
        this.destroy$.next(true);
        this.destroy$.unsubscribe();
    }
}
