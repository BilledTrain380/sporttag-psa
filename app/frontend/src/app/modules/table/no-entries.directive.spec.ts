import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { By } from "@angular/platform-browser";

import { NoEntriesDirective } from "./no-entries.directive";

describe("NoEntriesDirective", () => {
  let fixture: ComponentFixture<NoEntriesWrapperComponent>;
  let component: NoEntriesWrapperComponent;

  beforeEach(() => {
    TestBed.configureTestingModule({
                                     declarations: [
                                       NoEntriesWrapperComponent,
                                       NoEntriesDirective,
                                     ],
                                     imports: [
                                       CommonModule,
                                     ],
                                   })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NoEntriesWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should insert a no entries text when no entries are in the table", () => {
    const noEntries = fixture.debugElement.query(By.css("table tbody tr"));

    expect(noEntries)
      .not
      .toBeNull();

    expect(noEntries.nativeElement.innerText)
      .toBe("No entries");
  });

  it("should not insert a no entries text when entries are in the table", () => {
    component.titles = ["Doctor", "Master"];

    fixture.detectChanges();

    const entries = fixture.debugElement.queryAll(By.css("table tbody tr"));

    expect(entries.length)
      .withContext("Expected two table entries")
      // tslint:disable-next-line:no-magic-numbers
      .toBe(2);

    const firstEntry = entries[0];
    expect(firstEntry.nativeElement.innerText)
      .toBe("Doctor");

    const secondEntry = entries[1];
    expect(secondEntry.nativeElement.innerText)
      .toBe("Master");
  });
});

@Component({
             selector: "app-no-entries-wrapper",
             template: `
               <table appNoEntries [rowCount]="titles.length" columnCount="1">
                 <thead>
                 <tr>
                   <th>Title</th>
                 </tr>
                 </thead>
                 <tbody>
                 <tr *ngFor="let title of titles">
                   <td>{{title}}</td>
                 </tr>
                 </tbody>
               </table>
             `,
           })
class NoEntriesWrapperComponent {
  titles: Array<string> = [];
}
