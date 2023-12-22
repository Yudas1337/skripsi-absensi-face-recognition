<ul class="metismenu list-unstyled" id="side-menu">
    <li class="menu-title" key="t-menu">Beranda</li>

    <li>
        <a href="{{ route('dashboard.index') }}" class="waves-effect">
            <i class="bx bx-home-circle"></i>
            <span key="t-dashboards">Dashboard</span>
        </a>
    </li>

    <li class="menu-title" key="t-apps">Master Pengguna</li>

    <li>
        <a href="{{ route('employees.index') }}" class="waves-effect">
            <i class="bx bxs-user-detail"></i>
            <span key="t-dashboards">Pegawai</span>
        </a>
        <a href="{{ route('students.index') }}" class="waves-effect">
            <i class="bx bxs-face"></i>
            <span key="t-dashboards">Siswa Magang</span>
        </a>
        <a href="{{ route('instances.index') }}" class="waves-effect">
            <i class="bx bxs-school"></i>
            <span key="t-dashboards">Instansi</span>
        </a>
    </li>

    <li class="menu-title" key="t-apps">Master Presensi</li>
    <li>
        <a href="apps-filemanager.html" class="waves-effect">
            <i class="bx bx-file"></i>
            <span key="t-file-manager">Presensi Harian</span>
        </a>

        <a href="chat.html" class="waves-effect">
            <i class="bx bx-task"></i>
            <span key="t-chat">Rekap Presensi</span>
        </a>
        <a href="chat.html" class="waves-effect">
            <i class='bx bx-history'></i>
            <span key="t-chat">History Presensi</span>
        </a>
    </li>

    <li class="menu-title" key="t-pages">Pengaturan</li>
    <li>
        <a href="apps-filemanager.html" class="waves-effect">
            <i class="bx bx-stopwatch"></i>
            <span key="t-file-manager">Atur Jam Presensi</span>
        </a>

        <a href="chat.html" class="waves-effect">
            <i class="bx bx-task"></i>
            <span key="t-chat">Atur KeyCode</span>
        </a>
    </li>
</ul>
