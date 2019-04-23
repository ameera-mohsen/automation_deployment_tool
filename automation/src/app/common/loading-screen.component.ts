/*import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from "rxjs";
import { LoadingScreenService } from './loading-screen.service';

@Component({
  selector: 'app-loading-screen',
  templateUrl: './loading-screen.component.html',
  styleUrls: ['./loading-screen.component.scss']
})
export class LoadingScreenComponent implements OnInit, OnDestroy {

  loading: boolean = false;
  loadingSubscription: Subscription;

  constructor(private loadingScreenService: LoadingScreenService) {
  }

  ngOnInit() {
    this.loadingSubscription = this.loadingScreenService.loadingStatus.subscribe((value: any) => {
      this.loading = value;
    });
  }

  ngOnDestroy() {
    this.loadingSubscription.unsubscribe();
  }

}*/