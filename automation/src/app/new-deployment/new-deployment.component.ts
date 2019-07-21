import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-deployment',
  templateUrl: './new-deployment.component.html',
  styleUrls: ['./new-deployment.component.css']
})
export class NewDeploymentComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }
  
}
