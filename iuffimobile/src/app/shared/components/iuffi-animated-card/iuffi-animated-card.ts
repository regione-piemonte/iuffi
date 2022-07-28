import { animate, keyframes, state, style, transition, trigger } from '@angular/animations';
import { ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';

@Component({
    selector: 'iuffi-animated-card',
    templateUrl: './iuffi-animated-card.html',
    styleUrls: ['./iuffi-animated-card.scss'],
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
                            style({ height: '*', opacity: 1, offset: 1 })
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

export class IuffiAnimatedCardComponent implements OnInit {
    @Input() public isOpen = false;
    @Input() public class = '';
    @Input() public timingFunction = '';

    constructor(private change: ChangeDetectorRef) { }

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

    public onAnimationEnd(event: any): void {
        if (event.fromState === 'closed') {
        } else if (event.fromState === 'open') {
        }
    }

}
