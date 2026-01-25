import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should delete a session', () => {
    // ARRANGE
    service.delete('1').subscribe((response: any) => {
      expect(response).toBeNull();
    })
    
    // ASSERT
    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  })

  it('should create a session', () => {
    const newSession: Session = {
      id: 1,
      name: 'Session 1',
      description: 'Description 1',
      date: new Date(),
      teacher_id: 1,
      users: [1],
      createdAt: new Date(),
      updatedAt: new Date()
    }

    service.create(newSession).subscribe((session: Session) => {
      expect(session).toEqual(newSession);
    })

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newSession);
    req.flush(newSession);
  })

  it('should update a session', () => {
    const updatedSession: Session = {
      id: 1,
      name: 'UPDATED Session 1',
      description: 'Description updated',
      date: new Date(),
      teacher_id: 2,
      users: [5],
      createdAt: new Date(),
      updatedAt: new Date()
    }

    service.update('1', updatedSession).subscribe((session: Session) => {
      expect(session).toEqual(updatedSession);
    })

    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedSession);
    req.flush(updatedSession);
  })

  it('should participate to a session', () => {
    service.participate('1', '3').subscribe();

    const req = httpTestingController.expectOne('api/session/1/participate/3');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBeNull();
    req.flush(null);
  })

  it('should unparticipate to a session', () => {
    service.unParticipate('2','1').subscribe();

    const req = httpTestingController.expectOne('api/session/2/participate/1');
    expect(req.request.method).toBe('DELETE');
    expect(req.request.body).toBeNull();
    req.flush(null);
  })
});
