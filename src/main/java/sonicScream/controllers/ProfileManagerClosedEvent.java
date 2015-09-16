/*
 * The MIT License
 *
 * Copyright 2015 nmca.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package sonicScream.controllers;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import sonicScream.models.Profile;

class ProfileManagerClosedEvent extends Event
{
    Profile _profile;
    
    public Profile getProfile() { return _profile; }
    
    public static final EventType<ProfileManagerClosedEvent> PROFILE_RETURNED = new EventType<>(Event.ANY, "PROFILE_RETURNED");             

    public ProfileManagerClosedEvent(Profile profile)
    {
        super(null, null, PROFILE_RETURNED);
        _profile = profile;
    }
    
    public ProfileManagerClosedEvent(Object source, EventTarget target, Profile profile)
    {
        super(source, target, PROFILE_RETURNED);
        _profile = profile;
    }
    
    @Override
    public EventType<? extends ProfileManagerClosedEvent> getEventType()
    {
        return (EventType<? extends ProfileManagerClosedEvent>) super.getEventType();
    }
}
