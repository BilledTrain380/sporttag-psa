import {Directive, ElementRef, Renderer2} from '@angular/core';
import {NbLayoutDirectionService} from '@nebular/theme';

@Directive({
    selector: '[ngxAutoFloat]',
})
export class AutoFloatDirective {

    constructor(
        directionService: NbLayoutDirectionService,
        renderer: Renderer2,
        hostElement: ElementRef,
    ) {

        directionService.onDirectionChange()
            .subscribe(newDirection => {

                if (newDirection === 'ltr') {
                    renderer.addClass(hostElement.nativeElement, 'float-right');
                    renderer.removeClass(hostElement.nativeElement, 'float-left');
                }

                if (newDirection === 'rtl') {
                    renderer.addClass(hostElement.nativeElement, 'float-left');
                    renderer.removeClass(hostElement.nativeElement, 'float-right');
                }
            });
    }
}
