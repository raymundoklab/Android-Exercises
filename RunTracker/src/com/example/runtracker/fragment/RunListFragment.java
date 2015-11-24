
package com.example.runtracker.fragment;

import com.example.runtracker.R;
import com.example.runtracker.activity.RunActivity;
import com.example.runtracker.database.RunDatabaseHelper.RunCursor;
import com.example.runtracker.loader.RunListCursorLoader;
import com.example.runtracker.object.Run;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class RunListFragment extends ListFragment implements LoaderCallbacks< Cursor >
{
    public static final int REQUEST_NEW_RUN = 0;

    private static final String TAG = "RunListFragment";

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        switch( requestCode )
        {
            case REQUEST_NEW_RUN:
                getLoaderManager().restartLoader( 0, null, this );
                break;
            default:
                super.onActivityResult( requestCode, resultCode, data );
        }
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setHasOptionsMenu( true );
        getLoaderManager().initLoader( 0, null, this );
    }

    @Override
    public Loader< Cursor > onCreateLoader( int id, Bundle args )
    {
        return new RunListCursorLoader( getActivity() );
    }

    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater )
    {
        super.onCreateOptionsMenu( menu, inflater );
        inflater.inflate( R.menu.run_list_options, menu );
    }

    @Override
    public void onListItemClick( ListView l, View v, int position, long id )
    {
        Intent i = new Intent( getActivity(), RunActivity.class );
        i.putExtra( RunActivity.EXTRA_RUN_ID, id );
        startActivity( i );
    }

    @Override
    public void onLoaderReset( Loader< Cursor > loader )
    {
        setListAdapter( null );
    }

    @Override
    public void onLoadFinished( Loader< Cursor > loader, Cursor cursor )
    {
        RunCursorAdapter adapter = new RunCursorAdapter( getActivity(), ( RunCursor ) cursor );
        setListAdapter( adapter );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch( item.getItemId() )
        {
            case R.id.menu_item_new_run:
                Intent i = new Intent( getActivity(), RunActivity.class );
                startActivityForResult( i, REQUEST_NEW_RUN );
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    public class RunCursorAdapter extends CursorAdapter
    {
        private RunCursor mRunCursor;

        public RunCursorAdapter( Context context, RunCursor cursor )
        {
            super( context, cursor, 0 );
            mRunCursor = cursor;
        }

        @Override
        public void bindView( View view, Context context, Cursor cursor )
        {
            Run run = mRunCursor.getRun();
            TextView startDateView = ( TextView ) view;
            String cellText = context.getString( R.string.cell_text, run.getStartDate() );
            startDateView.setText( cellText );
        }

        @Override
        public View newView( Context context, Cursor cursor, ViewGroup parent )
        {
            LayoutInflater inflater = ( LayoutInflater ) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            return inflater.inflate( android.R.layout.simple_list_item_1, parent, false );
        }

    }
}
