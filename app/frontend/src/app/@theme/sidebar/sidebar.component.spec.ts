import { async, ComponentFixture, TestBed } from "@angular/core/testing";
import { By } from "@angular/platform-browser";
import { RouterTestingModule } from "@angular/router/testing";
import { faSquare } from "@fortawesome/free-solid-svg-icons";
import { NgbCollapseModule, NgbDropdownModule, NgbNavModule } from "@ng-bootstrap/ng-bootstrap";

import { CoreTestingModule } from "../../@core/core-testing-module";
import { PageMenu } from "../../@core/menu/page-menu";

import { SidebarComponent } from "./sidebar.component";

describe("SidebarComponent", () => {
  let component: SidebarComponent;
  let fixture: ComponentFixture<SidebarComponent>;

  const getSidebarToggle = () => fixture.debugElement.query(By.css('[data-test-selector="toggle-nav-button"]'));
  const ensureNavVisibility = () => {
    const nav = fixture.debugElement.query(By.css(".nav"));

    if (!nav.nativeElement.className.includes("show")) {
      getSidebarToggle().nativeElement
        .click();
      fixture.detectChanges();
    }
  };

  const testMenu: Array<PageMenu> = [
    {
      id: "1",
      title: "Normal menu item",
      icon: faSquare,
    },
    {
      id: "2",
      title: "Submenu item",
      icon: faSquare,
      children: [
        {
          id: "2-1",
          title: "Submenu 1",
        },
      ],
    },
  ];

  beforeEach(async(() => {
    TestBed.configureTestingModule({
                                     declarations: [SidebarComponent],
                                     imports: [
                                       CoreTestingModule,
                                       NgbNavModule,
                                       NgbCollapseModule,
                                       NgbDropdownModule,
                                       RouterTestingModule,
                                     ],
                                   })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SidebarComponent);
    component = fixture.componentInstance;
    component.items = testMenu;
    fixture.detectChanges();
    ensureNavVisibility();
  });

  afterEach(() => {
    ensureNavVisibility();
  });

  it("should create", () => {
    expect(component)
      .toBeDefined();
  });

  it("should toggle the sidebar when toggle button is clicked", () => {
    const sidebarToggle = getSidebarToggle();

    const isClassNameShowSet = () => fixture.debugElement
      .query(By.css(".nav"))
      .nativeElement.className
      .includes("show");

    expect(isClassNameShowSet())
      .withContext("Expected sidebar to be visible")
      .toBeTrue();

    sidebarToggle.nativeElement.click();
    fixture.detectChanges();

    expect(isClassNameShowSet())
      .withContext("Expected sidebar to be hidden")
      .toBeFalse();

    sidebarToggle.nativeElement.click();
    fixture.detectChanges();

    expect(isClassNameShowSet())
      .withContext("Expected sidebar to be visible again")
      .toBeTrue();
  });

  it("should toggle submenus when the parent menu item is clicked", () => {
    const submenuToggle = fixture.debugElement.query(By.css(".nav .nav-item .nav-link.submenu-toggle"));

    const isClassNameShowSet = () => fixture.debugElement
      .query(By.css('[data-test-selector="submenu"]'))
      .nativeElement.className
      .includes("show");

    expect(isClassNameShowSet())
      .withContext("Expected submenu to be hidden")
      .toBeFalse();

    submenuToggle.nativeElement.click();
    fixture.detectChanges();

    expect(isClassNameShowSet())
      .withContext("Expected submenu to be visible")
      .toBeTrue();

    submenuToggle.nativeElement.click();
    fixture.detectChanges();

    expect(isClassNameShowSet())
      .withContext("Expected submenu to be hidden again")
      .toBeFalse();
  });
});
