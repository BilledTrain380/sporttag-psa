import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckBoxTreeNodeComponent } from './check-box-tree-node.component';

describe('CheckBoxTreeNodeComponent', () => {
  let component: CheckBoxTreeNodeComponent;
  let fixture: ComponentFixture<CheckBoxTreeNodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CheckBoxTreeNodeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckBoxTreeNodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
