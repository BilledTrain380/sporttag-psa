import {Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {SearchSettings} from './search-settings';
import {FormControl} from '@angular/forms';
import {debounceTime, distinctUntilChanged, filter} from 'rxjs/operators';

@Component({
    selector: 'ngx-smart-search',
    templateUrl: './smart-search.component.html',
    styleUrls: ['./smart-search.component.scss'],
})
export class SmartSearchComponent implements OnInit {

    @Input()
    readonly settings: SearchSettings<any>;

    @Input()
    readonly source: ReadonlyArray<object>;

    @Output()
    readonly onSearchResultClick: EventEmitter<object> = new EventEmitter();

    readonly queryField: FormControl = new FormControl();

    searchResults: Array<SearchResult<object>> = [];

    @ViewChild('searchResultBox')
    private readonly searchBox: ElementRef;

    constructor() {
    }

    ngOnInit() {
        this.queryField.valueChanges
            .pipe(debounceTime(200))
            .pipe(distinctUntilChanged())
            .pipe(filter((term) => this.settings !== undefined))
            .subscribe((term: string) => {

                if (term && term.length < 3) {
                    this.clearSearch();
                    return;
                }

                this.searchResults = this.source
                    .filter(it => this.settings.matchFunction(term, it))
                    .slice(0, this.settings.limit + 1)
                    .map<SearchResult<object>>(value => {

                        const displayValue: string = this.settings.toStringValue(value);

                        return {
                            displayValue,
                            value,
                        };
                    });

                this.searchBox.nativeElement.classList.add('show');
            });
    }

    emit(value: object): void {
        this.onSearchResultClick.emit(value);
        this.clearSearch();
        this.queryField.reset();
    }

    private clearSearch(): void {
        this.searchBox.nativeElement.classList.remove('show');
        this.searchResults = [];
    }
}

interface SearchResult<T> {
    readonly displayValue: string;
    readonly value: T;
}
