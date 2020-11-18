import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RankingExportComponent } from './ranking-export.component';

describe('RankingExportComponent', () => {
  let component: RankingExportComponent;
  let fixture: ComponentFixture<RankingExportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RankingExportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RankingExportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
