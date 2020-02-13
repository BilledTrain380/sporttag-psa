import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LayoutButtonsComponent } from './layout-buttons.component';

describe('LayoutButtonsComponent', () => {
  let component: LayoutButtonsComponent;
  let fixture: ComponentFixture<LayoutButtonsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LayoutButtonsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LayoutButtonsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
