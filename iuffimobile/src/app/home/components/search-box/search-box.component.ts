import { animate, keyframes, state, style, transition, trigger } from '@angular/animations';
import { ChangeDetectorRef, Component, ElementRef, Input, Output, ViewChild } from '@angular/core';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { Animation } from '@ionic/angular';
import { Subject } from 'rxjs';

import { LoggerService } from './../../../core/logger/services/logger.service';

@Component({
    selector: 'search-box',
    templateUrl: 'search-box.component.html',
    styleUrls: ['search-box.component.scss'],
    animations: [
        trigger('listAnim', [
            state('open', style({ height: '*', opacity: 1 })),
            state('closed', style({ height: '0', opacity: 0 })),
            transition(
                'closed => open',
                [
                    animate(
                        '{{time}}',
                        keyframes([
                            style({ height: '0', opacity: 0, offset: 0 }),
                            style({ height: '*', opacity: 0.1, offset: 0.8 }),
                            style({ height: '90px', opacity: 1, offset: 1 })
                        ])
                    )
                ],
                { params: { time: '270ms ease-out' } }
            ),
            transition(
                'open => closed',
                [
                    animate(
                        '{{time}}',
                        keyframes([
                            style({ height: '*', opacity: 1, offset: 0 }),
                            style({ height: '*', opacity: 0.1, offset: 0.2 }),
                            style({ height: '0', opacity: 0, offset: 1 })
                        ])
                    )
                ],
                { params: { time: '220ms ease-out' } }
            )
        ])
    ]
})
export class SearchBox {
    public slideInTop!: Animation;
    public slideOutTop!: Animation;
    public searchForm!: FormGroup;
    public searching = false;
    @Input() public isOpen = false;
    @Input() public timingFunction = '';
    @Output() public onSearch = new Subject<any>();
    @Output() public onReset = new Subject<any>();
    @ViewChild('searchbox', { static: false }) private searchBox!: ElementRef;
    constructor(private logger: LoggerService, private change: ChangeDetectorRef,) {
        this.searchForm = new FormGroup({
            fromDate: new FormControl(''),
            toDate: new FormControl('')
        });
        this.searchForm.setValidators([checkIfEndDateAfterStartDate]);
    }

    ngOnInit(): void { }

    public open(): void {
        this.isOpen = true;
        this.change.detectChanges();
    }

    public toggle(): void {
        if (this.isOpen === false) {
            this.open();
        } else {
            this.close();
        }
    }

    public close(): void {
        this.isOpen = false;
        this.change.detectChanges();
    }

    ngOnChanges(): void {
        if (this.searchBox) {
            // this.slideInTop = createAnimation()
            //     .addElement(this.searchBox.nativeElement)
            //     .duration(300)
            //     .fromTo('opacity', '0', '1')
            //     .fromTo('height', '0', '90px');

            // this.slideOutTop = createAnimation()
            //     .addElement(this.searchBox.nativeElement)
            //     .duration(300)
            //     .fromTo('opacity', '1', '0')
            //     .fromTo('height', '90px', '0');
            // if (this.isOpen) {
            //     this.slideInTop.play();
            // }
            // else {
            //     this.searchForm.reset();
            //     this.slideOutTop.play();
            // }
        }
    }

    public search(): void {
        if (this.searchForm.valid && (this.searchForm.controls.fromDate.value || this.searchForm.controls.toDate.value)) {
            this.logger.debug('SearchBox::search');
            this.searching = true;
            this.onSearch.next(this.searchForm.getRawValue());
        }
    }

    public reset(): void {
        if (this.searchForm.valid) {
            this.logger.debug('SearchBox::reset');
            this.searching = false;
            this.searchForm.reset();
            this.onReset.next();
        }
    }
}

export function checkIfEndDateAfterStartDate(c: AbstractControl): any {
    if (c && c.get('fromDate') && c.get('toDate')) {
        //safety check
        if (!c.get('fromDate')?.value || !c.get('toDate')?.value) { return null }
        const startDate = new Date(c.get('fromDate')?.value);
        const endDate = new Date(c.get('toDate')?.value);
        if (startDate > endDate) {
            return { invalidEndDate: true }
        }
    }
    return null;
}
