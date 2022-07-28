import { NgModule } from '@angular/core';
import { NoPreloading, RouterModule, Routes } from '@angular/router';

const routes: Routes = [];

@NgModule({
    imports: [RouterModule.forRoot(routes, { preloadingStrategy: NoPreloading })],
    exports: [RouterModule]
})
export class AppRoutingModule { }
