import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPosts, Posts } from 'app/shared/model/posts.model';
import { PostsService } from './posts.service';
import { PostsComponent } from './posts.component';
import { PostsDetailComponent } from './posts-detail.component';
import { PostsUpdateComponent } from './posts-update.component';

@Injectable({ providedIn: 'root' })
export class PostsResolve implements Resolve<IPosts> {
  constructor(private service: PostsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPosts> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((posts: HttpResponse<Posts>) => {
          if (posts.body) {
            return of(posts.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Posts());
  }
}

export const postsRoute: Routes = [
  {
    path: '',
    component: PostsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Posts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PostsDetailComponent,
    resolve: {
      posts: PostsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Posts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PostsUpdateComponent,
    resolve: {
      posts: PostsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Posts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PostsUpdateComponent,
    resolve: {
      posts: PostsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Posts'
    },
    canActivate: [UserRouteAccessService]
  }
];
