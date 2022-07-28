import { Component, Input, OnInit } from '@angular/core';
import { IuffiAnimatedCardComponent } from '@shared/components/iuffi-animated-card/iuffi-animated-card';

@Component({
    selector: 'iuffi-animated-card-indicator',
    templateUrl: './iuffi-animated-card-indicator.component.html',
    styleUrls: ['./iuffi-animated-card-indicator.component.scss']
})
export class IuffiAnimatedCardIndicator implements OnInit {
  @Input() public icon = 'chevron-down';

  constructor(public animatedCard: IuffiAnimatedCardComponent) {}

  ngOnInit(): void {}
}
