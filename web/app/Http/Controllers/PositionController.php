<?php

namespace App\Http\Controllers;

use App\Models\Position;
use App\Http\Controllers\Controller;
use App\Http\Requests\StorePositionRequest;
use App\Http\Requests\UpdatePositionRequest;
use App\Contracts\Interfaces\PositionInterface;

class PositionController extends Controller
{
    private PositionInterface $position;

    public function __construct(PositionInterface $position)
    {
        $this->position = $position;
    }

    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $positions = $this->position->get();
        return view('menu.jabatan.index', compact('positions'));
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(StorePositionRequest $request)
    {
        $this->position->store($request->validated());
        return back()->with('success' , 'Jabatan Berhasil Ditambahkan');
    }

    /**
     * Display the specified resource.
     */
    public function show(Position $position)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(Position $position)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(UpdatePositionRequest $request, Position $position)
    {
        $this->position->update($position->id , $request->all());
        return back()->with('success' , 'Jabatan Berhasil Di Ubah');
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(Position $position)
    {
        $this->position->delete($position->id);
        return back()->with('success' , 'Jabatan Berhasil Di Hapus');
    }
}
