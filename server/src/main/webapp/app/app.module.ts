import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { ServerSharedModule } from 'app/shared/shared.module';
import { ServerCoreModule } from 'app/core/core.module';
import { ServerAppRoutingModule } from './app-routing.module';
import { ServerHomeModule } from './home/home.module';
import { ServerEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    ServerSharedModule,
    ServerCoreModule,
    ServerHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    ServerEntityModule,
    ServerAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class ServerAppModule {}
