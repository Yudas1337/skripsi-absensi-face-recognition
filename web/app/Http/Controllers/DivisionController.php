<?php

namespace App\Http\Controllers;

use App\Contracts\Interfaces\DivisionInterface;
use App\Models\division;
use App\Http\Requests\StoredivisionRequest;
use App\Http\Requests\UpdatedivisionRequest;

class DivisionController extends Controller
{
    private DivisionInterface $division;
    public function __construct(DivisionInterface $division)
    {
        return $this->division = $division;
    }
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $divisions = $this->division->get();
        return view('menu.division.index' , compact('divisions'));
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {

    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(StoredivisionRequest $request)
    {
        $this->division->store($request->validated());
        return back()->with('success' , 'Divisi Berhasil Ditambahkan');
    }

    /**
     * Display the specified resource.
     */
    public function show(division $division)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(division $division)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(UpdatedivisionRequest $request, division $division)
    {
        $this->division->update($division->id , $request->all());
        return back()->with('success' , 'Divisi Berhasil Di Ubah');
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(division $division)
    {
        $this->division->delete($division->id);
        return back()->with('success' , 'Divisi Berhasil Di Hapus');
    }
}
